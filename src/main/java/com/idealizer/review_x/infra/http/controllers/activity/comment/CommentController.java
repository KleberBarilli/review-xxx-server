package com.idealizer.review_x.infra.http.controllers.activity.comment;

import com.idealizer.review_x.application.activity.comment.usecases.CreateCommentUseCase;
import com.idealizer.review_x.application.activity.comment.usecases.RemoveCommentUseCase;
import com.idealizer.review_x.application.activity.comment.usecases.UpdateCommentUseCase;
import com.idealizer.review_x.common.dtos.activity.comment.CreateCommentDTO;
import com.idealizer.review_x.common.dtos.activity.comment.UpdateCommentDTO;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/activities/comments")
@Tag(name = "Comments")
public class CommentController {

    private final CreateCommentUseCase createCommentUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;
    private final RemoveCommentUseCase removeCommentUseCase;

    public CommentController(CreateCommentUseCase createCommentUseCase,
                             UpdateCommentUseCase updateCommentUseCase, RemoveCommentUseCase removeCommentUseCase) {
        this.createCommentUseCase = createCommentUseCase;
        this.updateCommentUseCase = updateCommentUseCase;
        this.removeCommentUseCase = removeCommentUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createComment(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CreateCommentDTO dto) {

        ObjectId userId = ((UserDetailsImpl) userDetails).getId();
        createCommentUseCase.execute(userId,
                userDetails.getUsername(),
                ((UserDetailsImpl) userDetails).getFullName(),
                new ObjectId(dto.targetId()),
                dto.targetType(),
                dto.content(),
                dto.spoiler());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateComment(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable() String id,
                              @Valid @RequestBody UpdateCommentDTO dto) {

        ObjectId userId = ((UserDetailsImpl) userDetails).getId();
        updateCommentUseCase.execute(
                new ObjectId(id),
                userId,
                userDetails.getUsername(),
                ((UserDetailsImpl) userDetails).getFullName(),
                dto.content(),
                dto.spoiler());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable() String id) {
        ObjectId userId = ((UserDetailsImpl) userDetails).getId();
        removeCommentUseCase.execute(new ObjectId(id), userId);
    }
}
