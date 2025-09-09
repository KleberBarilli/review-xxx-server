package com.idealizer.review_x.infra.persistence.mongo.implementations;

import com.idealizer.review_x.common.dtos.profile.game.FindProfileGamesDTO;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGame;
import com.idealizer.review_x.domain.core.profile.game.interfaces.BaseProfileGame;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepositoryCustom;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProfileGameRepositoryImpl implements ProfileGameRepositoryCustom {

    private final MongoTemplate coreMongo;
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    public ProfileGameRepositoryImpl( @Qualifier("coreMongoTemplate") MongoTemplate coreMongo) {
        this.coreMongo = coreMongo;
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
                "gameName", "gameSlug", "gameCover", "liked");

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
}
