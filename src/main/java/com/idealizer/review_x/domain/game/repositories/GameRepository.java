package com.idealizer.review_x.domain.game.repositories;

import com.idealizer.review_x.domain.game.entities.Game;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
    List<Game> findByIdIn(Set<ObjectId> ids);


    @Query(value = "{ 'slug': { $regex: ?0 } }")
    Page<Game> findBySlugRegex(String anchoredRegex, Pageable pageable);

    interface TotalOnly { long getTotal(); }

}
