package com.idealizer.review_x.application.activity.comment.usecases;

import com.idealizer.review_x.common.exceptions.NotFoundException;
import com.idealizer.review_x.domain.activity.comment.entities.Comment;
import com.idealizer.review_x.domain.activity.comment.repositories.CommentRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RemoveCommentUseCase {

    private final CommentRepository commentRepository;
    public RemoveCommentUseCase(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void execute(ObjectId commentId, ObjectId userId) {
        Comment comment = commentRepository.findByIdAndUserIdAndDeletedAtIsNull(commentId, userId).orElseThrow(
                () -> new NotFoundException("Comment not found")
        );
        comment.setContent(null);
        comment.setDeletedAt(Instant.now());
        commentRepository.save(comment);
    }
}
