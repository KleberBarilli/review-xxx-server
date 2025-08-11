package com.idealizer.review_x.domain.profile.game.repositories;

import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileReview;
import com.idealizer.review_x.domain.profile.game.entities.ReviewGame;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.Optional;

public interface ProfileReviewRepository extends MongoRepository<ReviewGame, ObjectId> {
    boolean existsByProfileGameId(ObjectId entryId);
    Optional<ReviewGame> findByProfileGameId(ObjectId profileGameId);

    long deleteByIdAndUserId(ObjectId reviewId, ObjectId userId);

    List<SimpleProfileReview> findTop5ByUserIdOrderByCreatedAtDesc(ObjectId userId);
    Optional<SimpleProfileReview> findProjectedByProfileGameId(ObjectId profileGameId);

    @Update("{ '$inc': { 'likes': ?1 } }")
    long findAndModifyById(ObjectId id, int delta);

}
