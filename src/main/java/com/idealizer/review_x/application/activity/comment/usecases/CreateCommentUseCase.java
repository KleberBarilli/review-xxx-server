package com.idealizer.review_x.application.activity.comment.usecases;

import com.idealizer.review_x.domain.activity.comment.entities.Comment;
import com.idealizer.review_x.domain.activity.comment.entities.CommentType;
import com.idealizer.review_x.domain.activity.comment.repositories.CommentRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class CreateCommentUseCase {
    private  final CommentRepository commentRepository;
    public CreateCommentUseCase(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void execute(ObjectId userId,String username, String fullName, ObjectId targetId, CommentType targetType, String content, Boolean spoiler) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setUsername(username);
        comment.setFullName(fullName);
        comment.setTargetId(targetId);
        comment.setTargetType(targetType);
        comment.setContent(content);
        comment.setSpoiler(spoiler);
        commentRepository.insert(comment);

    }
}
