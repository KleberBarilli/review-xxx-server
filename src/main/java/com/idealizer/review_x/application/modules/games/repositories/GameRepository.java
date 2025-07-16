package com.idealizer.review_x.application.modules.games.repositories;

import com.idealizer.review_x.application.modules.games.entities.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
}
