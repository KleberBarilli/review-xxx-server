package com.idealizer.review_x.application.games.profile.game.responses;

import com.idealizer.review_x.domain.core.profile.game.entities.PlatformType;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;
import com.idealizer.review_x.domain.game.entities.enums.GamePlatform;

import java.time.Instant;
import java.util.List;

public record PublicProfileGameDetailedResponse(
        String gameId,
        String gameName,
        String gameSlug,
        String gameCover,
        Integer rating,
        Boolean liked,
        Boolean mastered,
        Boolean owned,
        Boolean backlog,
        Boolean playing,
        Boolean wishlist,
        GamePlatform playedOn,
        Long playtimeMinutes,
        PlatformType sourcePlatform,
        Boolean hasReview,
        Instant startedAt,
        Instant finishedAt,
        ProfileGameStatus status,
        Integer favoriteOrder,
        Instant createdAt,
        Review review,
        List<Comment> comments
) {
    public record Review(
            String id,
            String content,
            Boolean spoiler,
            Boolean replay,
            long likeCount,
            Boolean likedByCurrentUser,
            Instant createdAt
    ) {}

    public record Comment(
            String username,
            String fullName,
            String content,
            Boolean spoiler,
            long likeCount,
            Instant createdAt,
            Instant editedAt
    ){}
}

