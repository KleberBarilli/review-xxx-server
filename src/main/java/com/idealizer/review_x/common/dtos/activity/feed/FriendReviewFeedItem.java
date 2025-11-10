package com.idealizer.review_x.common.dtos.activity.feed;

import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;
import com.idealizer.review_x.domain.game.entities.enums.GamePlatform;
import com.idealizer.review_x.domain.core.profile.game.entities.PlatformType;

import java.time.Instant;

public record FriendReviewFeedItem(
        String userId,
        String username,

        String targetName,
        String targetSlug,
        String targetCover,

        String reviewId,
        String title,
        String content,
        Boolean spoiler,
        Boolean replay,
        long likeCount,
        Instant createdAt,
        Instant updatedAt,

        Integer rating,
        Boolean liked,
        Boolean mastered,
        ProfileGameStatus status,
        GamePlatform playedOn,
        PlatformType sourcePlatform,
        Instant finishedAt
) {}
