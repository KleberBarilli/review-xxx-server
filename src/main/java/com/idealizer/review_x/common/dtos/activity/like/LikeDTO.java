package com.idealizer.review_x.common.dtos.activity.like;

import com.idealizer.review_x.domain.core.activity.like.entities.LikeType;
import io.swagger.v3.oas.annotations.media.Schema;

public record LikeDTO(
        @Schema(description = "Target like id", requiredMode = Schema.RequiredMode.REQUIRED)
        String targetId,
        @Schema(description = "Target like type", requiredMode = Schema.RequiredMode.REQUIRED, example = "REVIEW")
        LikeType targetType) {
}
