package com.idealizer.review_x.infra.persistence.mongo.implementations;

import com.idealizer.review_x.common.dtos.FindTimelineDTO;
import com.idealizer.review_x.common.dtos.profile.game.FindProfileGamesDTO;
import com.idealizer.review_x.domain.core.profile.BaseTimelineItem;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGame;
import com.idealizer.review_x.domain.core.profile.game.interfaces.BaseProfileGame;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepositoryCustom;
import com.idealizer.review_x.domain.core.review.entities.Review;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class ProfileGameRepositoryImpl implements ProfileGameRepositoryCustom {

    private final MongoTemplate coreMongo;
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    public ProfileGameRepositoryImpl( @Qualifier("coreMongoTemplate") MongoTemplate coreMongo) {
        this.coreMongo = coreMongo;
    }
    static class RawAggregationOperation implements AggregationOperation {
        private final Document doc;
        RawAggregationOperation(Document doc) { this.doc = doc; }
        @Override public Document toDocument(AggregationOperationContext ctx) { return doc; }
    }

    @Override
    public Page<BaseProfileGame> searchProjected(String username, FindProfileGamesDTO f, Pageable pageable) {
        List<Criteria> and = new ArrayList<>();
        and.add(Criteria.where("username").is(username));

        if (f.statuses() != null && !f.statuses().isEmpty())
            and.add(Criteria.where("status").in(f.statuses()));

        if (f.wishlist() != null)
            and.add(Criteria.where("wishlist").is(f.wishlist()));
        if (f.owned() != null)
            and.add(Criteria.where("owned").is(f.owned()));
        if (f.mastered() != null)
            and.add(Criteria.where("mastered").is(f.mastered()));
        if (f.liked() != null)
            and.add(Criteria.where("liked").is(f.liked()));

        if (f.backlog() != null)
            and.add(Criteria.where("backlog").is(f.backlog()));

        if (f.playing() != null)
            and.add(Criteria.where("playing").is(f.playing()));

        if (f.ratingMin() != null || f.ratingMax() != null) {
            Criteria r = Criteria.where("rating");
            if (f.ratingMin() != null)
                r = r.gte(f.ratingMin());
            if (f.ratingMax() != null)
                r = r.lte(f.ratingMax());
            and.add(r);
        }

        Query q = new Query(new Criteria().andOperator(and.toArray(Criteria[]::new)));

        q.fields().include("_id", "status", "createdAt", "updatedAt", "rating", "owned", "wishlist", "mastered",
                "gameName", "gameSlug", "gameCover", "liked", "backlog", "playing");

        Sort sort = resolveSort(f.sort(), f.order()).and(Sort.by(Sort.Direction.DESC, "_id"));
        Pageable effective = PageRequest.of(
                Math.max(pageable.getPageNumber(), 0),
                Math.max(pageable.getPageSize(), 1),
                sort);
        q.with(effective);

        long total = coreMongo.count(Query.of(q).limit(-1).skip(-1).with(Sort.unsorted()), ProfileGame.class);

        List<ProfileGame> docs = coreMongo.find(q, ProfileGame.class, coreMongo.getCollectionName(ProfileGame.class));
        List<BaseProfileGame> content = docs.stream()
                .map(doc -> projectionFactory.createProjection(BaseProfileGame.class, doc))
                .toList();

        return new PageImpl<>(content, effective, total);
    }

    private Sort resolveSort(String sort, String order) {
        Sort.Direction dir = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String field = switch (sort == null ? "" : sort) {
            case "createdAt" -> "createdAt";
            case "name" -> "gameName";
            case "favoriteOrder" -> "favoriteOrder";
            case "rating" -> "rating";
            default -> "updatedAt";
        };
        return Sort.by(dir, field);
    }
    @Override
    public Page<BaseTimelineItem> timeline(String username, FindTimelineDTO f, Pageable pageable) {
        List<AggregationOperation> base = new ArrayList<>();

        base.add(Aggregation.match(Criteria.where("username").is(username)));

        Document startedObj = new Document("type", "STARTED").append("at", "$started_at");
        Document finishedObj = new Document("type", "FINISHED").append("at", "$finished_at");

        Document startedCond = new Document("$cond", Arrays.asList(
                new Document("$ne", Arrays.asList("$started_at", null)),
                startedObj,
                null
        ));

        Document finishedCond = new Document("$cond", Arrays.asList(
                new Document("$ne", Arrays.asList("$finished_at", null)),
                finishedObj,
                null
        ));

        base.add(new RawAggregationOperation(new Document("$addFields",
                new Document("events",
                        new Document("$setDifference", Arrays.asList(
                                Arrays.asList(startedCond, finishedCond),
                                Arrays.asList((Object) null)
                        ))
                ))
        ));

        base.add(Aggregation.unwind("$events"));

        // 3. Filtros globais (Ano, Tipo)
        if (f != null) {
            if (f.types() != null && !f.types().isEmpty()) {
                base.add(Aggregation.match(Criteria.where("events.type").in(f.types())));
            }
            if (f.year() != null) {
                LocalDate from = LocalDate.of(f.year(), 1, 1);
                LocalDate to = from.plusYears(1);
                base.add(Aggregation.match(Criteria.where("events.at")
                        .gte(Date.from(from.atStartOfDay(ZoneOffset.UTC).toInstant()))
                        .lt(Date.from(to.atStartOfDay(ZoneOffset.UTC).toInstant()))));
            }
        }

        int page = Math.max(pageable.getPageNumber(), 0);
        int size = Math.max(pageable.getPageSize(), 1);

        List<AggregationOperation> dataTail = new ArrayList<>();

        dataTail.add(Aggregation.sort(Sort.by(Sort.Direction.DESC, "events.at")
                .and(Sort.by(Sort.Direction.DESC, "_id"))));

        dataTail.add(Aggregation.skip((long) page * size));
        dataTail.add(Aggregation.limit(size));

        dataTail.add(new RawAggregationOperation(new Document("$project",
                new Document()
                        .append("id", "$_id")
                        .append("targetName", "$game_name")
                        .append("targetSlug", "$game_slug")
                        .append("targetCover", "$game_cover")
                        .append("rating", "$rating")
                        .append("liked", "$liked")
                        .append("mastered", "$mastered")
                        .append("hasReview",  "$has_review")
                        .append("status", "$status")
                        .append("playedOn", "$played_on")
                        .append("sourcePlatform", "$source_platform")
                        .append("eventType", "$events.type")
                        .append("eventAt", "$events.at")
        )));

        Aggregation countAgg = Aggregation.newAggregation(new ArrayList<>(base) {{
            add(Aggregation.count().as("total"));
        }});

        Aggregation dataAgg = Aggregation.newAggregation(Stream.concat(base.stream(), dataTail.stream()).toList());

        String collName = coreMongo.getCollectionName(ProfileGame.class);

        var dataRes = coreMongo.aggregate(dataAgg, collName, Document.class);
        List<BaseTimelineItem> content = dataRes.getMappedResults().stream()
                .map(doc -> projectionFactory.createProjection(BaseTimelineItem.class, doc))
                .toList();

        var cntRes = coreMongo.aggregate(countAgg, collName, Document.class).getUniqueMappedResult();
        long total = (cntRes == null) ? 0L : ((Number) cntRes.get("total")).longValue();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }
}


