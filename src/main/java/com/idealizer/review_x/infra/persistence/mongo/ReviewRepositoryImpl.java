package com.idealizer.review_x.infra.persistence.mongo;

import com.idealizer.review_x.common.dtos.review.FindReviewDTO;
import com.idealizer.review_x.domain.profile.game.entities.ProfileGame;
import com.idealizer.review_x.domain.review.entities.Review;
import com.idealizer.review_x.domain.review.interfaces.BaseReview;
import com.idealizer.review_x.domain.review.repositories.ReviewRepositoryCustom;
import org.bson.Document;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Stream;

@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final MongoTemplate template;
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    public ReviewRepositoryImpl(MongoTemplate template) {
        this.template = template;
    }

    static class RawAggregationOperation implements AggregationOperation {
        private final Document doc;
        RawAggregationOperation(Document doc) { this.doc = doc; }
        @Override public Document toDocument(AggregationOperationContext ctx) { return doc; }
    }

    @Override
    public Page<BaseReview> searchProjected(String username, FindReviewDTO f, Pageable pageable) {
        List<AggregationOperation> pre = new ArrayList<>();

        pre.add(Aggregation.match(Criteria.where("username").is(username)));

        String pgColl = template.getCollectionName(ProfileGame.class);
        pre.add(new RawAggregationOperation(new Document("$lookup",
                new Document("from", pgColl)
                        .append("let", new Document("uid", "$userId").append("gid", "$gameId"))
                        .append("pipeline", List.of(
                                new Document("$match", new Document("$expr",
                                        new Document("$and", List.of(
                                                new Document("$eq", List.of("$userId", "$$uid")),
                                                new Document("$eq", List.of("$gameId", "$$gid"))
                                        ))
                                )),
                                new Document("$project",
                                        new Document("_id", 0)
                                                .append("rating", 1)
                                                .append("liked", 1)
                                                .append("status", 1)
                                )
                        ))
                        .append("as", "pg")
        )));

        pre.add(new RawAggregationOperation(new Document("$addFields",
                new Document("pg",
                        new Document("$arrayElemAt", Arrays.asList("$pg", 0))
                )
        )));

        pre.add(new RawAggregationOperation(new Document("$addFields",
                new Document("rating", "$pg.rating")
                        .append("liked", "$pg.liked")
                        .append("status", "$pg.status")
        )));

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
            case "rating"    -> "rating";       // já está no topo após $addFields
            default          -> "updated_at";
        };
        Sort.Direction dir = "asc".equalsIgnoreCase(f.order()) ? Sort.Direction.ASC : Sort.Direction.DESC;

        List<AggregationOperation> sortAndPage = new ArrayList<>();

        if ("rating".equals(sortDbField)) {
            Number sentinel = dir.isAscending() ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            sortAndPage.add(new RawAggregationOperation(new Document("$addFields",
                    new Document("ratingSort",
                            new Document("$ifNull", Arrays.asList("$rating", sentinel))
                    )
            )));
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

                        .append("targetName", new Document("$ifNull", Arrays.asList("$targetName", "$target_name")))
                        .append("targetSlug", new Document("$ifNull", Arrays.asList("$targetSlug", "$target_slug")))
                        .append("targetCover", new Document("$ifNull", Arrays.asList("$targetCover", "$target_cover")))
                        .append("targetType", new Document("$ifNull", Arrays.asList("$targetType", "$target_type")))

                        .append("createdAt", new Document("$ifNull", Arrays.asList("$createdAt", "$created_at")))
                        .append("updatedAt", new Document("$ifNull", Arrays.asList("$updatedAt", "$updated_at")))
                        .append("startedAt", new Document("$ifNull", Arrays.asList("$startedAt", "$started_at")))
                        .append("finishedAt", new Document("$ifNull", Arrays.asList("$finishedAt", "$finished_at")))

                        .append("rating", 1)
                        .append("liked", 1)
                        .append("status", 1)
        )));

        Aggregation countAgg = Aggregation.newAggregation(new ArrayList<>(pre) {{
            add(Aggregation.count().as("total"));
        }});

        Aggregation dataAgg = Aggregation.newAggregation(
                Stream.concat(pre.stream(), sortAndPage.stream()).toList()
        );

        String coll = template.getCollectionName(Review.class);

        var dataRes = template.aggregate(dataAgg, coll, Document.class);
        List<BaseReview> content = dataRes.getMappedResults().stream()
                .map(doc -> projectionFactory.createProjection(BaseReview.class, doc))
                .toList();

        var cntRes = template.aggregate(countAgg, coll, Document.class).getUniqueMappedResult();
        long total = (cntRes == null) ? 0L : ((Number) cntRes.get("total")).longValue();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }
}