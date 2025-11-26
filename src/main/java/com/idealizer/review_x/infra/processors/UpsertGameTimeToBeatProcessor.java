package com.idealizer.review_x.infra.processors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.core.provider.entities.PlatformType;
import com.idealizer.review_x.domain.core.provider.entities.Provider;
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
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class UpsertGameTimeToBeatProcessor {
    private static final Logger logger = Logger.getLogger(UpsertGameTimeToBeatProcessor.class.getName());

    private static final String IGDB_BASE_URL = "https://api.igdb.com/v4";
    private static final int BATCH_LIMIT = 500;
    private static final Duration HTTP_TIMEOUT = Duration.ofSeconds(20);

    int REFRESH_AFTER_DAYS = 30;
    Date refreshThreshold = Date.from(java.time.Instant.now().minus(java.time.Duration.ofDays(REFRESH_AFTER_DAYS)));

    private final MongoTemplate gamesMongo;
    private final MongoTemplate coreMongo;;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${TWITCH_CLIENT_ID}")
    private String clientId;

    Instant now = Instant.now();


    public UpsertGameTimeToBeatProcessor(@Qualifier("gamesMongoTemplate") MongoTemplate gamesMongo,
                                         @Qualifier("coreMongoTemplate") MongoTemplate coreMongo) {
        this.coreMongo = coreMongo;
        this.gamesMongo = gamesMongo;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @PostConstruct
    public void init() {
        logger.info("UpsertGameTimeToBeatProcessor initialized");
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

    @Scheduled(cron = "0 48 17 * * *", zone = "America/Sao_Paulo")
    public void updateGames() {
        logger.info("Starting game TTB backfill from IGDB...");
        final String accessToken = getAccessToken();
        logger.info("Access token retrieved successfully.");


        Criteria staleTtb = new Criteria().orOperator(
                Criteria.where("time_to_beat.updated_at").exists(false),
                Criteria.where("time_to_beat.updated_at").lt(refreshThreshold)
        );

        Criteria noTtb = new Criteria().orOperator(
                Criteria.where("time_to_beat").exists(false),
                Criteria.where("time_to_beat").is(null),
                new Criteria().orOperator(
                        Criteria.where("time_to_beat.normally").exists(false),
                        Criteria.where("time_to_beat.completely").exists(false),
                        Criteria.where("time_to_beat.hastily").exists(false)
                )
        );

        Criteria baseFilter = new Criteria().andOperator(noTtb, staleTtb);

        long total = gamesMongo.count(new Query(baseFilter), Game.class);
        logger.info("Total de jogos a preencher TTB: " + total);
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

            q.fields().include("_id").include("igdb_id");

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
                    String body = buildTtbApicalypse(igdbIds);
                    List<IgdbTtb> ttbs = postAndParse(
                            IGDB_BASE_URL + "/game_time_to_beats",
                            body,
                            accessToken,
                            new TypeReference<List<IgdbTtb>>() {}
                    );

                    Map<Long, IgdbTtb> byGameId = new HashMap<>();
                    if (ttbs != null) {
                        for (IgdbTtb t : ttbs) byGameId.put(t.game_id, t);
                    }

                    BulkOperations ops = gamesMongo.bulkOps(BulkOperations.BulkMode.UNORDERED, Game.class);

                    for (Game g : batch) {
                        Long gid = safeGetIgdbId(g);
                        if (gid == null) continue;

                        IgdbTtb t = byGameId.get(gid);


                        boolean hasAny =
                                t != null && (t.normally != null || t.completely != null || t.hastily != null);


                        Map<String, Object> ttb = new HashMap<>();
                        if(hasAny){
                            if (t.normally != null)   ttb.put("normally", t.normally);
                            if (t.completely != null) ttb.put("completely", t.completely);
                            if (t.hastily != null)    ttb.put("hastily", t.hastily);
                            if( t.count != null)    ttb.put("count", t.count);
                        }
                        ttb.put("updated_at", now);

                        Update up = new Update().set("time_to_beat", ttb);
                        ops.updateOne(new Query(Criteria.where("_id").is(safeGetObjectId(g))), up);
                    }

                    var res = ops.execute();
                    logger.info(String.format("Bulk TTB: modified=%d", res.getModifiedCount()));

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

        logger.info("Backfill de TTB concluído.");
    }


    private String buildTtbApicalypse(List<Long> ids) {
        String idList = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        return "fields game_id, normally, completely, hastily,count;"
                + " where game_id = (" + idList + ");"
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
    static class IgdbTtb {
        public long game_id;
        public Integer normally;
        public Integer completely;
        public Integer hastily;
        public Integer count;
    }

    static class RateLimitException extends Exception {
        private final long retryAfterMs;
        public RateLimitException(long retryAfterMs) { this.retryAfterMs = retryAfterMs; }
        public long retryAfterMs() { return retryAfterMs; }
    }
}