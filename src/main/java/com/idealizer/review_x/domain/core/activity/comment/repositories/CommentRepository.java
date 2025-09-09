package com.idealizer.review_x.domain.core.activity.comment.repositories;

import com.idealizer.review_x.domain.core.activity.comment.entities.Comment;
import com.idealizer.review_x.domain.core.activity.comment.entities.CommentType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {
    Optional<Comment> findByIdAndUserIdAndDeletedAtIsNull(ObjectId id, ObjectId userId);
    List<Comment> findAllByTargetIdAndTargetTypeAndDeletedAtIsNull(ObjectId targetId, CommentType targetType);
}
