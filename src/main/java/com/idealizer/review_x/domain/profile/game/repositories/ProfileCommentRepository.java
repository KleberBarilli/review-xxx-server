package com.idealizer.review_x.domain.profile.game.repositories;

import com.idealizer.review_x.domain.profile.game.entities.ReviewComment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileCommentRepository extends MongoRepository<ReviewComment, ObjectId> {
}
