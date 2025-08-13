package com.idealizer.review_x.common.dtos.activity.comment;

import com.idealizer.review_x.domain.activity.comment.entities.CommentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record CreateCommentDTO(
        @Schema(description = "Ex, reviewId", requiredMode = Schema.RequiredMode.REQUIRED)
        String targetId,
        @Schema(example = "REVIEW", requiredMode = Schema.RequiredMode.REQUIRED)
        CommentType targetType,
        @Schema(description = "Content of the comment", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 1, max = 2000)
        String content,
        @Schema(description = "Indicates if the comment contains spoilers")
        Boolean spoiler) {
}
