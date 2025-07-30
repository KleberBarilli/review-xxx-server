package com.idealizer.review_x.domain.game.repositories;

import com.idealizer.review_x.domain.game.entities.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
}
