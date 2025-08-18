package com.idealizer.review_x.application.activity.comment.usecases;

import com.idealizer.review_x.common.exceptions.NotFoundException;
import com.idealizer.review_x.domain.activity.comment.entities.Comment;
import com.idealizer.review_x.domain.activity.comment.repositories.CommentRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateCommentUseCase {
    private final CommentRepository commentRepository;

    public UpdateCommentUseCase(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void execute(ObjectId id, ObjectId userId, String username, String fullName, String content, Boolean spoiler) {
        Comment c = commentRepository.findByIdAndUserIdAndDeletedAtIsNull(id, userId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        c.setContent(content);
        c.setUsername(username);
        c.setFullName(fullName);
        c.setSpoiler(spoiler);
        c.setEditedAt(Instant.now());
        commentRepository.save(c);

    }
}
