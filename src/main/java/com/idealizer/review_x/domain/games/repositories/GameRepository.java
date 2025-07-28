package com.idealizer.review_x.domain.games.repositories;

import com.idealizer.review_x.domain.games.entities.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
}
