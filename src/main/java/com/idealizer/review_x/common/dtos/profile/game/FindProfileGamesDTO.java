package com.idealizer.review_x.common.dtos.profile.game;

import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;

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
        Boolean backlog,
        Boolean playing,
        Integer ratingMin,
        Integer ratingMax
) {
}


