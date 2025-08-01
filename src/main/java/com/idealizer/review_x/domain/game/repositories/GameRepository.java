package com.idealizer.review_x.domain.game.repositories;

import com.idealizer.review_x.domain.game.entities.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
    List<Game> findByIdIn(Set<ObjectId> ids);

}
