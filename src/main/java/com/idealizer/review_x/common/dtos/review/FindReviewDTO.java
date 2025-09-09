package com.idealizer.review_x.common.dtos.review;

import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;

import java.util.Set;

public record FindReviewDTO(
        int pageNumber,
        int limit,
        String sort,
        String order,
        Set<ProfileGameStatus> statuses,
        Integer ratingMin,
        Integer ratingMax) {
}
