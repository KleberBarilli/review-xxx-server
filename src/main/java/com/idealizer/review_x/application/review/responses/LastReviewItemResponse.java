package com.idealizer.review_x.application.review.responses;

import com.idealizer.review_x.domain.core.LogID;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;

import java.time.Instant;

public record LastReviewItemResponse(String targetId,
        String profileTargetId,
        String targetName,
        String targetSlug,
        String targetCover,
        LogID type,
        Boolean liked,
        Integer rating,
        ProfileGameStatus status,
        String content,
        Boolean spoiler,
        Integer likes,
        Instant createdAt) {
}
