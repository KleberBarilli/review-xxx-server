package com.idealizer.review_x.domain.core.activity.like.repositories;

import com.idealizer.review_x.domain.core.activity.like.entities.Like;
import com.idealizer.review_x.domain.core.activity.like.entities.LikeType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like, ObjectId> {

    long deleteByUserIdAndTargetIdAndTargetType(ObjectId userId, ObjectId targetId, LikeType targetType);
    boolean existsByTargetIdAndUserId(ObjectId reviewId, ObjectId userId);
}
