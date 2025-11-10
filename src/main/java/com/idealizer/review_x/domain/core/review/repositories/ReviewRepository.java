package com.idealizer.review_x.domain.core.review.repositories;

import com.idealizer.review_x.domain.core.LogID;
import com.idealizer.review_x.domain.core.review.entities.Review;
import com.idealizer.review_x.domain.core.profile.game.interfaces.SimpleProfileReview;
import com.idealizer.review_x.domain.core.review.interfaces.SimpleReview;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, ObjectId>, ReviewRepositoryCustom {
    boolean existsByProfileTargetIdAndTargetType(ObjectId targetId, LogID targetType);
    Optional<Review> findByProfileTargetIdAndTargetType(ObjectId profileGameId, LogID targetType);

    long deleteByIdAndUserId(ObjectId reviewId, ObjectId userId);

    List<SimpleProfileReview> findTop5ByUserIdOrderByCreatedAtDesc(ObjectId userId);
    Optional<SimpleProfileReview> findProjectedByProfileTargetIdAndTargetType(ObjectId profileGameId ,LogID targetType);

    @Update("{ '$inc': { 'likes': ?1 } }")
    long findAndModifyById(ObjectId id, int delta);

    Page<SimpleReview> findProjectedByUserIdIn(List<ObjectId> userIds, Pageable pageable);
}
