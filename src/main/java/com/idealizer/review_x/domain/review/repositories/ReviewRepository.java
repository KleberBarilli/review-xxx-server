package com.idealizer.review_x.domain.review.repositories;

import com.idealizer.review_x.domain.LogID;
import com.idealizer.review_x.domain.review.entities.Review;
import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileReview;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    boolean existsByProfileTargetIdAndTargetType(ObjectId targetId, LogID targetType);
    Optional<Review> findByProfileTargetIdAndTargetType(ObjectId profileGameId, LogID targetType);

    long deleteByIdAndUserId(ObjectId reviewId, ObjectId userId);

    List<SimpleProfileReview> findTop5ByUserIdOrderByCreatedAtDesc(ObjectId userId);
    Optional<SimpleProfileReview> findProjectedByProfileTargetIdAndTargetType(ObjectId profileGameId ,LogID targetType);

    @Update("{ '$inc': { 'likes': ?1 } }")
    long findAndModifyById(ObjectId id, int delta);

}
