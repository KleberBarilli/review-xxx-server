package com.idealizer.review_x.domain.profile.game.repositories;

import com.idealizer.review_x.domain.profile.game.review.ReviewGame;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileReviewRepository extends MongoRepository<ReviewGame, ObjectId> {
    boolean existsByProfileGameId(ObjectId entryId);
}
