package com.idealizer.review_x.common.dtos.profile.game;

import com.idealizer.review_x.domain.profile.game.entities.ProfileGameStatus;

import java.util.List;
import java.util.Set;

// filtros e paginação
public record FindProfileGamesDTO(
        int pageNumber,
        int limit,
        String sort,          // updatedAt | whenAdded | lastPlayed | name | favoriteOrder | rating
        String order,         // asc | desc
        Set<ProfileGameStatus> statuses,
        Boolean wishlist,
        Boolean owned,
        Boolean mastered,
        Boolean liked,
        Integer ratingMin,    // 1..10
        Integer ratingMax     // 1..10
) {}


