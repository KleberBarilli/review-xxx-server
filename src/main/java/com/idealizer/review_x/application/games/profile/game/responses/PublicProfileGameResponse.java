package com.idealizer.review_x.application.games.profile.game.responses;

import com.idealizer.review_x.domain.profile.game.entities.ProfileGameStatus;

import java.time.Instant;

public record PublicProfileGameResponse(
        String gameId,
        String gameName,
        String gameSlug,
        String gameCover,
        Integer rating,
        Boolean liked,
        Boolean hasReview,
        ProfileGameStatus status,
        Integer favoriteOrder,
        Instant createdAt,
        Review review
) {
    public record Review(
            String content,
            Boolean spoiler,
            Boolean replay,
            Integer likes,
            Instant createdAt
    ) {}
}

