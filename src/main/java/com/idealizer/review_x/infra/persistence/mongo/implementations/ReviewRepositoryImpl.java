package com.idealizer.review_x.infra.persistence.mongo.implementations;

import com.idealizer.review_x.common.dtos.FindTimelineDTO;
import com.idealizer.review_x.common.dtos.review.FindReviewDTO;
import com.idealizer.review_x.domain.core.profile.BaseTimelineItem;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGame;
import com.idealizer.review_x.domain.core.review.entities.Review;
import com.idealizer.review_x.domain.core.review.interfaces.BaseReview;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepositoryCustom;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

        private final MongoTemplate coreMongo;
        private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

        public ReviewRepositoryImpl(@Qualifier("coreMongoTemplate") MongoTemplate coreMongo) {
                this.coreMongo = coreMongo;
        }

        static class RawAggregationOperation implements AggregationOperation {
                private final Document doc;
                RawAggregationOperation(Document doc) { this.doc = doc; }
                @Override public Document toDocument(AggregationOperationContext ctx) { return doc; }
        }

        private static Document ifNull(Document a, Object b) { return new Document("$ifNull", Arrays.asList(a, b)); }
        private static Document field(String name) { return new Document("$ifNull", Arrays.asList("$" + name, null)); }

        @Override
        public Page<BaseReview> searchProjected(String username, FindReviewDTO f, Pageable pageable) {
                List<AggregationOperation> pre = new ArrayList<>();

                pre.add(Aggregation.match(Criteria.where("username").is(username)));

                String pgColl = coreMongo.getCollectionName(ProfileGame.class);

                Document uidExpr = ifNull(field("userId"), "$user_id");

                Document gid1 = ifNull(field("gameId"), "$game_id");
                Document gid2 = ifNull(field("targetId"), "$target_id");
                Document gidExpr = new Document("$ifNull", Arrays.asList(gid1, gid2));

                pre.add(new RawAggregationOperation(new Document("$lookup",
                        new Document("from", pgColl)
                                .append("let", new Document("uid", uidExpr).append("gid", gidExpr))
                                .append("pipeline", List.of(
                                        new Document("$match", new Document("$expr",
                                                new Document("$and", List.of(
                                                        new Document("$or", List.of(
                                                                new Document("$eq", List.of(
                                                                        new Document("$toString", "$userId"),
                                                                        new Document("$toString", "$$uid")
                                                                )),
                                                                new Document("$eq", List.of(
                                                                        new Document("$toString", "$user_id"),
                                                                        new Document("$toString", "$$uid")
                                                                ))
                                                        )),
                                                        new Document("$or", List.of(
                                                                new Document("$eq", List.of(
                                                                        new Document("$toString", "$gameId"),
                                                                        new Document("$toString", "$$gid")
                                                                )),
                                                                new Document("$eq", List.of(
                                                                        new Document("$toString", "$game_id"),
                                                                        new Document("$toString", "$$gid")
                                                                ))
                                                        ))
                                                ))
                                        )),
                                        new Document("$project",
                                                new Document("_id", 0)
                                                        .append("rating", 1)
                                                        .append("liked", 1)
                                                        .append("mastered", 1)
                                                        .append("status", 1)
                                                        .append("updated_at", 1)
                                        )
                                ))
                                .append("as", "pg")
                )));

                pre.add(new RawAggregationOperation(new Document("$addFields",
                        new Document("pg", new Document("$arrayElemAt", Arrays.asList("$pg", 0))))
                ));

                pre.add(new RawAggregationOperation(new Document("$addFields",
                        new Document("rating", new Document("$ifNull", Arrays.asList("$pg.rating", "$rating")))
                                .append("liked",  new Document("$ifNull", Arrays.asList("$pg.liked",  "$liked")))
                                .append("mastered",  new Document("$ifNull", Arrays.asList("$pg.mastered",  "$mastered")))
                                .append("status", new Document("$ifNull", Arrays.asList("$pg.status", "$status"))))
                ));

                if (f.statuses() != null && !f.statuses().isEmpty()) {
                        pre.add(Aggregation.match(Criteria.where("status").in(f.statuses())));
                }
                if (f.ratingMin() != null || f.ratingMax() != null) {
                        Criteria rc = new Criteria("rating");
                        if (f.ratingMin() != null) rc = rc.gte(f.ratingMin());
                        if (f.ratingMax() != null) rc = rc.lte(f.ratingMax());
                        pre.add(Aggregation.match(rc));
                }

                int page = Math.max(pageable.getPageNumber(), 0);
                int size = Math.max(pageable.getPageSize(), 1);

                String sortUi = (f.sort() == null ? "" : f.sort());
                String sortDbField = switch (sortUi) {
                        case "startedAt" -> "started_at";
                        case "finishedAt" -> "finished_at";
                        case "rating" -> "rating";
                        default -> "updated_at";
                };
                Sort.Direction dir = "asc".equalsIgnoreCase(f.order()) ? Sort.Direction.ASC : Sort.Direction.DESC;

                List<AggregationOperation> sortAndPage = new ArrayList<>();

                if ("rating".equals(sortDbField)) {
                        Number sentinel = dir.isAscending() ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                        sortAndPage.add(new RawAggregationOperation(new Document("$addFields",
                                new Document("ratingSort", new Document("$ifNull", Arrays.asList("$rating", sentinel))))
                        ));
                        sortAndPage.add(Aggregation.sort(Sort.by(dir, "ratingSort").and(Sort.by(Sort.Direction.DESC, "_id"))));
                } else {
                        sortAndPage.add(Aggregation.sort(Sort.by(dir, sortDbField).and(Sort.by(Sort.Direction.DESC, "_id"))));
                }

                sortAndPage.add(Aggregation.skip((long) page * size));
                sortAndPage.add(Aggregation.limit(size));

                sortAndPage.add(new RawAggregationOperation(new Document("$project",
                        new Document()
                                .append("id", "$_id")
                                .append("title", 1)
                                .append("content", 1)
                                .append("spoiler", 1)
                                .append("replay", 1)
                                .append("targetName",  new Document("$ifNull", Arrays.asList("$targetName",  "$target_name")))
                                .append("targetSlug",  new Document("$ifNull", Arrays.asList("$targetSlug",  "$target_slug")))
                                .append("targetCover", new Document("$ifNull", Arrays.asList("$targetCover", "$target_cover")))
                                .append("targetType",  new Document("$ifNull", Arrays.asList("$targetType",  "$target_type")))
                                .append("createdAt",   new Document("$ifNull", Arrays.asList("$createdAt",   "$created_at")))
                                .append("updatedAt",   new Document("$ifNull", Arrays.asList("$updatedAt",   "$updated_at")))
                                .append("startedAt",   new Document("$ifNull", Arrays.asList("$startedAt",   "$started_at")))
                                .append("finishedAt",  new Document("$ifNull", Arrays.asList("$finishedAt",  "$finished_at")))
                                .append("rating", "$rating")
                                .append("liked",  "$liked")
                                .append("mastered", "$mastered")
                                .append("status", "$status")
                )));

                Aggregation countAgg = Aggregation.newAggregation(new ArrayList<>(pre) {{ add(Aggregation.count().as("total")); }});
                Aggregation dataAgg  = Aggregation.newAggregation(Stream.concat(pre.stream(), sortAndPage.stream()).toList());

                String coll = coreMongo.getCollectionName(Review.class);

                var dataRes = coreMongo.aggregate(dataAgg, coll, Document.class);
                List<BaseReview> content = dataRes.getMappedResults().stream()
                        .map(doc -> projectionFactory.createProjection(BaseReview.class, doc))
                        .toList();

                var cntRes = coreMongo.aggregate(countAgg, coll, Document.class).getUniqueMappedResult();
                long total = (cntRes == null) ? 0L : ((Number) cntRes.get("total")).longValue();

                return new PageImpl<>(content, PageRequest.of(page, size), total);
        }
}