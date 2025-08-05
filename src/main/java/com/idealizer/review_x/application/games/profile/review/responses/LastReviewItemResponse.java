package com.idealizer.review_x.application.games.profile.review.responses;

import com.idealizer.review_x.domain.profile.game.ProfileGameStatus;

import java.time.Instant;

public record LastReviewItemResponse(String gameId,
                                     String profileGameId,
                                     String gameName,
                                     String gameSlug,
                                     String gameCover,
                                     Boolean liked,
                                     Integer rating,
                                     ProfileGameStatus status,
                                     String content,
                                     Boolean spoiler,
                                     Integer likes,
                                     Instant createdAt
) {
}
