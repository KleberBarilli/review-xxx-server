package com.idealizer.review_x.domain.profile.game.repositories;

import com.idealizer.review_x.domain.profile.game.entities.ReviewGame;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileReviewLikeRepository extends MongoRepository<ReviewGame, ObjectId> {
}
