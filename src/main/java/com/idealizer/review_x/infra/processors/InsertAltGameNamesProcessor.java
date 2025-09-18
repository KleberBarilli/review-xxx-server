package com.idealizer.review_x.infra.processors;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.core.provider.entities.PlatformType;
import com.idealizer.review_x.domain.core.provider.entities.Provider;
import com.idealizer.review_x.common.helpers.NormalizeAliases;
import jakarta.annotation.PostConstruct;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class InsertAltGameNamesProcessor {
    private static final Logger logger = Logger.getLogger(InsertAltGameNamesProcessor.class.getName());

    private static final String IGDB_BASE_URL = "https://api.igdb.com/v4";
    private static final int BATCH_LIMIT = 500;
    private static final Duration HTTP_TIMEOUT = Duration.ofSeconds(20);


    private final MongoTemplate coreMongo;
    private final MongoTemplate gamesMongo;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${TWITCH_CLIENT_ID}")
    private String clientId;


    public InsertAltGameNamesProcessor( @Qualifier("coreMongoTemplate") MongoTemplate coreMongo,
                                        @Qualifier("gamesMongoTemplate") MongoTemplate gamesMongo) {
        this.gamesMongo = gamesMongo;
        this.coreMongo = coreMongo;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @PostConstruct
    public void init() {
        logger.info("InsertAltGameNamesProcessor initialized");
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")));
    }
    private String getAccessToken() {
        Query query = new Query(Criteria.where("platform").is(PlatformType.TWITCH));
        Provider provider = coreMongo.findOne(query, Provider.class, "providers");
        if (provider == null) {
            throw new IllegalStateException("Provider not found for platform: TWITCH");
        }
        return provider.getAccessToken();
    }

    @Scheduled(cron = "0 44 20 * * *", zone = "America/Sao_Paulo")
    public void updateGames() {
        logger.info("Starting game ALT backfill from IGDB...");
        final String accessToken = getAccessToken();
        logger.info("Access token retrieved successfully.");
        Criteria baseFilter = new Criteria().orOperator(
                Criteria.where("alternative_names").exists(false),
                Criteria.where("alternative_names").is(null),
                Criteria.where("alternative_names").size(0)
        );

        long total = gamesMongo.count(new Query(baseFilter), Game.class);
        logger.info("Total de jogos a preencher ALT: " + total);
        if (total == 0) return;

        final int LIMIT = BATCH_LIMIT;
        ObjectId lastId = null;

        while (true) {
            Criteria pageCrit = (lastId == null)
                    ? baseFilter
                    : new Criteria().andOperator(baseFilter, Criteria.where("_id").gt(lastId));

            Query q = new Query(pageCrit)
                    .with(Sort.by(Sort.Direction.ASC, "_id"))
                    .limit(LIMIT);

            q.fields().include("_id").include("igdb_id").include("name").include("slug");

            List<Game> batch = gamesMongo.find(q, Game.class);
            if (batch.isEmpty()) break;

            logger.info(String.format("Processando lote (%,d itens) a partir de _id>%s",
                    batch.size(), lastId == null ? "null" : lastId.toHexString()));


            List<Long> igdbIds = batch.stream()
                    .map(this::safeGetIgdbId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            if (!igdbIds.isEmpty()) {
                try {
                    String body = buildAltNamesApicalypse(igdbIds);
                    List<IgdbAltNames> alts = postAndParse(
                            IGDB_BASE_URL + "/alternative_names",
                            body,
                            accessToken,
                            new TypeReference<List<IgdbAltNames>>() {}
                    );

                    Map<Long, List<Map<String, Object>>> altByGame = Collections.emptyMap();
                    if (alts != null && !alts.isEmpty()) {
                        altByGame = alts.stream()
                                .filter(a -> a.game != null && a.name != null && !a.name.isBlank())
                                .collect(Collectors.groupingBy(
                                        a -> a.game,
                                        Collectors.collectingAndThen(
                                                Collectors.toList(),
                                                list -> {                                      
                                                    Map<String, Map<String, Object>> uniq = new LinkedHashMap<>();
                                                    for (IgdbAltNames a : list) {
                                                        String name = a.name.trim();
                                                        if (name.isEmpty()) continue;
                                                        String comment = a.comment == null ? null : a.comment.trim();

                                                        String key = name.toLowerCase(Locale.ROOT) + "\u0000" +
                                                                (comment == null ? "" : comment.toLowerCase(Locale.ROOT));

                                                        if (!uniq.containsKey(key)) {
                                                            Map<String, Object> doc = new LinkedHashMap<>();
                                                            doc.put("name", name);
                                                            if (comment != null && !comment.isEmpty()) {
                                                                doc.put("comment", comment);
                                                            }
                                                            uniq.put(key, doc);
                                                        }
                                                    }
                                                    return new ArrayList<>(uniq.values());
                                                }
                                        )
                                ));
                    }

                    BulkOperations ops = gamesMongo.bulkOps(BulkOperations.BulkMode.UNORDERED, Game.class);

                    for (Game g : batch) {
                        ObjectId oid = safeGetObjectId(g);
                        if (oid == null) continue;

                        Long gid = safeGetIgdbId(g);
                        if (gid == null) continue;

                        List<Map<String, Object>> items = altByGame.getOrDefault(gid, Collections.emptyList());
                        LinkedHashSet<String> aliasesSet = new LinkedHashSet<>();
                        try {
                            String slug = g.getSlug();
                            if (slug != null && !slug.isBlank()) aliasesSet.add(slug);
                        } catch (Exception ignored) {}

                        try { addAliases(aliasesSet, g.getName()); } catch (Exception ignored) {}
                        for (Map<String, Object> it : items) {
                            Object n = it.get("name");
                            if (n instanceof String ns) addAliases(aliasesSet, ns);
                        }

                        List<String> aliases = new ArrayList<>(aliasesSet);
                        if (aliases.size() > MAX_ALIASES) aliases = aliases.subList(0, MAX_ALIASES);

                        Update up = new Update();
                        if (!items.isEmpty()) {
                            up.set("alternative_names", items);
                            up.set("aliases", aliases);
                        } else {
                            up.unset("alternative_names");
                            up.unset("aliases");
                        }


                        ops.updateOne(new Query(Criteria.where("_id").is(oid)), up);
                    }

                    var res = ops.execute();
                    logger.info(String.format("Bulk ALT NAMES: modified=%d", res.getModifiedCount()));

                } catch (RateLimitException rle) {
                    logger.warning("IGDB 429 Rate Limit. Aguardando " + rle.retryAfterMs() + "ms e repetindo o mesmo lote...");
                    sleepQuiet(rle.retryAfterMs());
                    continue;
                } catch (Exception e) {
                    logger.warning("Falha no lote: " + e.getMessage());

                }
            }

            Object last = batch.get(batch.size() - 1).getId();
            ObjectId candidate = toObjectId(last);
            if (candidate == null) {
                logger.warning("Não foi possível avançar o cursor por _id; encerrando para evitar loop.");
                break;
            }
            lastId = candidate;

            sleepQuiet(300);
        }

        logger.info("Backfill de ALT NAMES concluído.");
    }


    private String buildAltNamesApicalypse(List<Long> ids) {
        String idList = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        return "fields comment,game,name;"
                + " where game = (" + idList + ");"
                + " limit " + Math.min(ids.size(), BATCH_LIMIT) + ";";
    }

    private <T> T postAndParse(String url, String igdbQuery, String token, TypeReference<T> typeRef) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(HTTP_TIMEOUT)
                .header("Client-ID", clientId)
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(igdbQuery))
                .build();

        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = resp.statusCode();

        if (status == 429) {
            long retryMs = parseRetryAfterMs(resp.headers().firstValue("Retry-After").orElse("2"));
            throw new RateLimitException(retryMs);
        }
        if (status < 200 || status >= 300) {
            throw new RuntimeException("IGDB HTTP " + status + " body=" + resp.body());
        }
        return objectMapper.readValue(resp.body(), typeRef);
    }

    private long parseRetryAfterMs(String v) {
        try { return (long) (Double.parseDouble(v) * 1000L); }
        catch (Exception e) { return 2000L; }
    }

    private void sleepQuiet(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }

    private Long safeGetIgdbId(Game g) {
        try { return g.getIgdbId(); } catch (Exception e) { return null; }
    }

    private ObjectId safeGetObjectId(Game g) {
        Object raw = g.getId();
        return toObjectId(raw);
    }

    private ObjectId toObjectId(Object raw) {
        if (raw instanceof ObjectId) return (ObjectId) raw;
        if (raw instanceof String s) {
            try { return new ObjectId(s); } catch (Exception ignored) { return null; }
        }
        return null;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    static class IgdbAltNames {
        public Long game;
        public String comment;
        public String name;
    }

    static class RateLimitException extends Exception {
        private final long retryAfterMs;
        public RateLimitException(long retryAfterMs) { this.retryAfterMs = retryAfterMs; }
        public long retryAfterMs() { return retryAfterMs; }
    }
    private static final int MAX_ALIASES = 64;
    private static void addAliases(LinkedHashSet<String> acc, String s) {
        if (s == null || s.isBlank()) return;
        for (String cand : NormalizeAliases.slugCandidates(s)) {
            if (cand != null && !cand.isBlank()) acc.add(cand);
        }
    }



}
