package com.idealizer.review_x.application.modules.games.services;

import com.idealizer.review_x.application.modules.games.controllers.dto.FindGamesResponseDTO;

public interface FindManyGamesService {
    FindGamesResponseDTO execute(int limit, int offset, String sortBy, String order);
}
