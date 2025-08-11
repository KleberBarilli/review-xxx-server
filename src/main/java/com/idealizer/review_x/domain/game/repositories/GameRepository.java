package com.idealizer.review_x.domain.game.repositories;

import com.idealizer.review_x.domain.game.entities.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
    List<Game> findByIdIn(Set<ObjectId> ids);


    @Aggregation(pipeline = {
            "{ $search: { index: 'autocomplete_idx', autocomplete: { query: ?0, path: 'slug', tokenOrder: 'sequential', fuzzy: { maxEdits: 1, prefixLength: 2 } } } }",
            "{ $addFields: { score: { $meta: 'searchScore' } } }",
            "{ $sort: { total_rating_count: -1, score: -1 } }",
            "{ $skip: ?1 }",
            "{ $limit: ?2 }",
            "{ $project: { score: 0 } }"   // remove only the temp field; everything else stays
    })
    List<Game> autocompleteSlug(String query, int skip, int limit);

    // Total hits for the same filter (needed for pagination)
    @Aggregation(pipeline = {
            "{ $searchMeta: { index: 'autocomplete_idx', autocomplete: { query: ?0, path: 'slug'," +
                    " tokenOrder: 'sequential', fuzzy: { maxEdits: 1, prefixLength: 2 } }, count: { type: 'total' } } }",
            "{ $project: { total: '$count.total' } }"
    })
    List<TotalOnly> autocompleteSlugCount(String query);

    interface TotalOnly { long getTotal(); }

}
