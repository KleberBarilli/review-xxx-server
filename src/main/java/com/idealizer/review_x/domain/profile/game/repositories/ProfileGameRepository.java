package com.idealizer.review_x.domain.profile.game.repositories;

import com.idealizer.review_x.domain.profile.game.ProfileGame;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileGameRepository extends MongoRepository<ProfileGame, ObjectId> {
    Optional<ProfileGame> findByGameId(ObjectId gameId);
}
