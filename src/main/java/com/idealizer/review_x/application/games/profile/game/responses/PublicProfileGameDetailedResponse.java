package com.idealizer.review_x.application.games.profile.game.responses;

import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;

import java.time.Instant;
import java.util.List;

public record PublicProfileGameDetailedResponse(
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
        Review review,
        List<Comment> comments,
        List<Log> logs
) {
    public record Review(
            String id,
            String content,
            Boolean spoiler,
            Boolean replay,
            long likeCount,
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
    public record Log(
            int year,
            int month,
            int day,
            int minutesPlayed,
            String note
    ){}
}

