package com.idealizer.review_x.application.games.profile.game.responses;

import com.idealizer.review_x.domain.profile.game.entities.ProfileGameStatus;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

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
        Review review,
        List<Comment> comments
) {
    public record Review(
            ObjectId id,
            String content,
            Boolean spoiler,
            Boolean replay,
            Integer likeCount,
            Instant createdAt
    ) {}

    public record Comment(
            String username,
            String content,
            Boolean spoiler,
            Integer likeCount,
            Instant createdAt,
            Instant editedAt
    ){}
}

