package com.idealizer.review_x.common.dtos.profile.game;

import com.idealizer.review_x.domain.profile.game.entities.ProfileGameStatus;

import java.util.List;
import java.util.Set;

public record FindProfileGamesDTO(
        int pageNumber,
        int limit,
        String sort,
        String order,
        Set<ProfileGameStatus> statuses,
        Boolean wishlist,
        Boolean owned,
        Boolean mastered,
        Boolean liked,
        Integer ratingMin,
        Integer ratingMax
) {
}


