package com.idealizer.review_x.domain.user.repositories;

import com.idealizer.review_x.domain.user.entities.Like;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like, ObjectId> {

    long deleteByReviewIdAndUserId(ObjectId reviewId, ObjectId userId);
    boolean existsByReviewIdAndUserId(ObjectId reviewId, ObjectId userId);
}
