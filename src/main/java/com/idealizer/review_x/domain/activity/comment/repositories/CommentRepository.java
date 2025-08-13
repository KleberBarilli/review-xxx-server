package com.idealizer.review_x.domain.activity.comment.repositories;

import com.idealizer.review_x.domain.activity.comment.entities.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {
    Optional<Comment> findByIdAndUserIdAndDeletedAtIsNull(ObjectId id, ObjectId userId);
}
