package com.idealizer.review_x.application.games.profile.game.responses;

import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;

import java.time.Instant;

public record PublicProfileGameResponse(
        String id,
        String name,
        String slug,
        String cover,
        Integer rating,
        Boolean liked,
        ProfileGameStatus status,
        Instant updatedAt
) {
}
