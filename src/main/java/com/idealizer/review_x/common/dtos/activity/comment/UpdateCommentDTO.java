package com.idealizer.review_x.common.dtos.activity.comment;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateCommentDTO(
        @Schema(description = "Content of the comment", requiredMode = Schema.RequiredMode.REQUIRED)
        String content,
        @Schema(description = "Indicates if the comment contains spoilers")
        Boolean spoiler) {
}
