package com.idealizer.review_x.modules.games;

import com.idealizer.review_x.modules.games.entities.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
}
