package com.idealizer.review_x.domain.core.profile.game.repositories;

import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileGameLogRepository extends MongoRepository<ProfileGameLog, ObjectId> {
    Optional<ProfileGameLog> findByIdAndUserId(ObjectId id, ObjectId userId);
    List<ProfileGameLog> findByProfileGameIdAndUserId(ObjectId profileGameId, ObjectId userId);
    long deleteByIdAndUserId(ObjectId id, ObjectId userId);
}
