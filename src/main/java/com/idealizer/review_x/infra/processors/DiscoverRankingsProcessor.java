package com.idealizer.review_x.infra.processors;

import com.idealizer.review_x.common.dtos.game.DiscoverPreset;
import com.idealizer.review_x.infra.libs.twitch.igdb.services.IgdbPopularityClient;
import jakarta.xml.bind.DatatypeConverter;
import org.bson.Document;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class DiscoverRankingsProcessor {

    private static final Logger log = Logger.getLogger(DiscoverRankingsProcessor.class.getName());

    private static final String F_IGDB_ID = "igdb_id";
    private static final String F_NAME = "name";
    private static final String F_SLUG = "slug";
    private static final String F_COVER = "cover";
    private static final String F_FIRST_RELEASE_DATE = "first_release_date";
    private static final String F_TOTAL_RATING = "total_rating";
    private static final String F_TOTAL_RATING_COUNT = "total_rating_count";
    private static final String F_POPULARITY = "popularity";
    private static final String F_FOLLOWS = "follows";
    private static final String F_HYPES = "hypes";

    private static final String RANKINGS_COLL = "game_rankings";
    private static final String GAMES_COLL = "games";
    private static final int TOP_K = 50;

    private final MongoTemplate mongo;
    private final IgdbPopularityClient igdb;

    public DiscoverRankingsProcessor(MongoTemplate mongo, IgdbPopularityClient igdb) {
        this.mongo = mongo;
        this.igdb = igdb;
    }

    @Scheduled(cron = "0 11 18 * * *", zone = "America/Sao_Paulo")
    public void runDaily() {
        long t0 = nowMs();
        long gamesCount = mongo.count(new Query(), GAMES_COLL);
        log.info("[discover] starting processor | topK=" + TOP_K + " | games.count=" + gamesCount);

        try {
            rebuildTopRated();
        } catch (Exception e) {
            log.log(Level.SEVERE, "[discover] TOP_RATED failed", e);
        }

        for (DiscoverPreset p : DiscoverPreset.values()) {
            if (p == DiscoverPreset.TOP_RATED) continue;
            try {
                rebuildFromPopularityPrimitive(p, TOP_K);
            } catch (Exception e) {
                log.log(Level.SEVERE, "[discover] preset " + p.name() + " failed", e);
            }
        }

        log.info("[discover] processor done in " + tookMs(t0) + " ms");
    }

    public void rebuildFromPopularityPrimitive(DiscoverPreset preset, int k) {
        long tPreset = nowMs();
        log.info("[discover:" + preset.name() + "] fetching primitives | typeId=" + preset.popularityTypeId() + " | limit=" + k);

        long tCall = nowMs();
        List<IgdbPopularityClient.PopularityPrimitive> prim = igdb.topKByType(preset.popularityTypeId(), k);
        log.info("[discover:" + preset.name() + "] igdb primitives: " + (prim == null ? 0 : prim.size()) + " | " + tookMs(tCall) + " ms");

        if (prim == null || prim.isEmpty()) {
            var del = mongo.remove(new Query(Criteria.where("preset").is(preset.name())), RANKINGS_COLL);
            log.warning("[discover:" + preset.name() + "] empty primitives -> cleared preset | deleted=" + (del != null ? del.getDeletedCount() : 0));
            log.info("[discover:" + preset.name() + "] done in " + tookMs(tPreset) + " ms");
            return;
        }

        List<Long> ids = prim.stream().map(IgdbPopularityClient.PopularityPrimitive::gameId).toList();
        List<Document> games = findLocalGamesByIgdbIds(ids);
        log.info("[discover:" + preset.name() + "] local games fetched=" + games.size() + " / " + ids.size());

        Set<Long> foundIds = games.stream().map(this::readIgdbId).filter(Objects::nonNull).collect(Collectors.toSet());
        List<Long> missing = ids.stream().filter(id -> !foundIds.contains(id)).toList();
        if (!missing.isEmpty()) {
            log.warning("[discover:" + preset.name() + "] missing in local db: " + missing.size() + " (showing up to 10) -> " + sample(missing, 10));
        }

        Map<Long, Document> byId = games.stream()
                .collect(Collectors.toMap(this::readIgdbId, d -> d, (a,b)->a, LinkedHashMap::new));

        List<Document> toInsert = new ArrayList<>(prim.size());
        Instant now = Instant.now();
        int rank = 1;

        for (IgdbPopularityClient.PopularityPrimitive p : prim) {
            Document g = byId.get(p.gameId());
            if (g == null) continue;

            Document gameSnap = new Document()
                    .append("name", readString(g, F_NAME, "name"))
                    .append("slug", readString(g, F_SLUG, "slug"))
                    .append("cover", readString(g, F_COVER, "cover"))
                    .append("firstReleaseDate", readDate(g, F_FIRST_RELEASE_DATE, "firstReleaseDate"));


            Document doc = new Document()
                    .append("preset", preset.name())
                    .append("igdbId", p.gameId())
                    .append("rank", rank++)
                    .append("score", p.value())
                    .append("generatedAt", Date.from(now))
                    .append("game", gameSnap);

            toInsert.add(doc);
        }

        log.info("[discover:" + preset.name() + "] prepared docs=" + toInsert.size()
                + " | top preview=" + previewTop(toInsert, 3));

        long tReplace = nowMs();
        var del = mongo.remove(new Query(Criteria.where("preset").is(preset.name())), RANKINGS_COLL);
        int deleted = del != null ? (int) del.getDeletedCount() : 0;

        int insertedCount = 0;
        if (!toInsert.isEmpty()) {
            try {
                Collection<Document> res = mongo.insert(toInsert, RANKINGS_COLL);
                insertedCount = res.size();
            } catch (DataAccessException dae) {
                log.log(Level.SEVERE, "[discover:" + preset.name() + "] insert failed", dae);
                throw dae;
            }
        }

        log.info("[discover:" + preset.name() + "] replaced | deleted=" + deleted
                + " inserted=" + insertedCount + " | " + tookMs(tReplace) + " ms");
        log.info("[discover:" + preset.name() + "] done in " + tookMs(tPreset) + " ms");
    }

    public void rebuildTopRated() {
        long tPreset = nowMs();
        log.info("[discover:TOP_RATED] computing…");

        Instant now = Instant.now();
        Query base = new Query(new Criteria()
                .and(F_FIRST_RELEASE_DATE).lte(Date.from(now))
                .and(F_TOTAL_RATING_COUNT).gte(100));

        base.fields().include(F_IGDB_ID, F_NAME, F_SLUG, F_COVER,
                F_FIRST_RELEASE_DATE, F_TOTAL_RATING, F_TOTAL_RATING_COUNT);
        base.with(Sort.by(Sort.Direction.DESC, F_TOTAL_RATING_COUNT));
        base.limit(20_000);

        long tFind = nowMs();
        List<Document> pool = mongo.find(base, Document.class, GAMES_COLL);
        log.info("[discover:TOP_RATED] pool size=" + pool.size() + " | " + tookMs(tFind) + " ms");

        long tScore = nowMs();
        record Row(long igdbId, double score, Document g) {}
        List<Row> scored = new ArrayList<>(pool.size());
        for (Document g : pool) {
            double rating = toDouble(readNumber(g, F_TOTAL_RATING, "totalRating"));
            double votes  = toDouble(readNumber(g, F_TOTAL_RATING_COUNT, "totalRatingCount"));
            double score  = rating * Math.log10(votes + 1d);
            Long id = readIgdbId(g);
            if (id == null) continue;
            scored.add(new Row(id, score, g));
        }
        scored.sort(Comparator.comparingDouble((Row r) -> r.score).reversed());
        log.info("[discover:TOP_RATED] scored+sorted | " + tookMs(tScore) + " ms");

        List<Document> toInsert = new ArrayList<>();
        Instant gen = Instant.now();
        int limit = Math.min(TOP_K, scored.size());
        for (int i = 0; i < limit; i++) {
            Row r = scored.get(i);
            Document g = r.g();

            Document gameSnap = new Document()
                    .append("name", readString(g, F_NAME, "name"))
                    .append("slug", readString(g, F_SLUG, "slug"))
                    .append("cover", readString(g, F_COVER, "cover"))
                    .append("firstReleaseDate", readDate(g, F_FIRST_RELEASE_DATE, "firstReleaseDate"));

            Document signals = new Document()
                    .append("totalRating", readNumber(g, F_TOTAL_RATING, "totalRating"))
                    .append("totalRatingCount", readNumber(g, F_TOTAL_RATING_COUNT, "totalRatingCount"));

            toInsert.add(new Document()
                    .append("preset", DiscoverPreset.TOP_RATED.name())
                    .append("igdbId", r.igdbId())
                    .append("rank", i + 1)
                    .append("score", r.score())
                    .append("generatedAt", Date.from(gen))
                    .append("game", gameSnap)
                    .append("signals", signals));
        }

        log.info("[discover:TOP_RATED] prepared docs=" + toInsert.size()
                + " | top preview=" + previewTop(toInsert, 3));

        long tReplace = nowMs();
        var del = mongo.remove(new Query(Criteria.where("preset").is(DiscoverPreset.TOP_RATED.name())), RANKINGS_COLL);
        int deleted = del != null ? (int) del.getDeletedCount() : 0;

        int insertedCount = 0;
        if (!toInsert.isEmpty()) {
            Collection<Document> res = mongo.insert(toInsert, RANKINGS_COLL);
            insertedCount = res.size();
        }

        log.info("[discover:TOP_RATED] replaced | deleted=" + deleted
                + " inserted=" + insertedCount + " | " + tookMs(tReplace) + " ms");
        log.info("[discover:TOP_RATED] done in " + tookMs(tPreset) + " ms");
    }


    /** Busca por múltiplos nomes possíveis do campo de ID (igdb_id, igdbId, externalId…). */
    private List<Document> findLocalGamesByIgdbIds(List<Long> ids) {
        Criteria or = new Criteria().orOperator(
                Criteria.where(F_IGDB_ID).in(ids),
                Criteria.where("igdbId").in(ids),
                Criteria.where("externalId").in(ids),
                Criteria.where("igdb_id").in(ids)
        );
        Query q = new Query(or);
        q.fields().include(F_IGDB_ID, "igdbId", "externalId",
                F_NAME, F_SLUG, F_COVER, F_FIRST_RELEASE_DATE,
                F_TOTAL_RATING, F_TOTAL_RATING_COUNT, F_POPULARITY, F_FOLLOWS, F_HYPES,
                "firstReleaseDate", "totalRating", "totalRatingCount", "popularity", "follows", "hypes",
                "coverImageId");
        return mongo.find(q, Document.class, GAMES_COLL);
    }

    private Long readIgdbId(Document d) {
        Object v = firstNonNull(d, F_IGDB_ID, "igdbId", "externalId", "igdb_id");
        if (v instanceof Number n) return n.longValue();
        if (v != null) try { return Long.parseLong(v.toString()); } catch (Exception ignored) {}
        return null;
    }

    private Object firstNonNull(Document d, String... keys) {
        for (String k : keys) {
            if (d.containsKey(k) && d.get(k) != null) return d.get(k);
        }
        return null;
    }

    private Number readNumber(Document d, String... keys) {
        Object v = firstNonNull(d, keys);
        return (v instanceof Number n) ? n : null;
    }

    private String readString(Document d, String... keys) {
        Object v = firstNonNull(d, keys);
        return v != null ? String.valueOf(v) : null;
    }

    private Date readDate(Document d, String... keys) {
        Object v = firstNonNull(d, keys);
        if (v instanceof Date dt) return dt;
        if (v instanceof CharSequence s) {
            try { return DatatypeConverter.parseDateTime(s.toString()).getTime(); } catch (Exception ignored) {}
        }
        if (v instanceof Number n) return new Date(n.longValue());
        return null;
    }

    private static double toDouble(Number n) { return n == null ? 0d : n.doubleValue(); }

    private static long nowMs() { return System.currentTimeMillis(); }
    private static String tookMs(long start) { return String.valueOf(System.currentTimeMillis() - start); }

    private static String previewTop(List<Document> docs, int n) {
        if (docs == null || docs.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < Math.min(n, docs.size()); i++) {
            var d = docs.get(i);
            var game = (Document) d.get("game");
            sb.append("#").append(d.get("rank")).append(" ")
                    .append(game != null ? game.getString("name") : d.get("igdbId"))
                    .append(" (score=").append(d.get("score")).append(")");
            if (i < n - 1 && i < docs.size() - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    private static <T> String sample(List<T> list, int n) {
        if (list == null || list.isEmpty()) return "[]";
        return list.subList(0, Math.min(n, list.size())).toString();
    }
}