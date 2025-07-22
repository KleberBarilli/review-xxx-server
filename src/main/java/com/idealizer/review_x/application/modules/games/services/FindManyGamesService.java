package com.idealizer.review_x.application.modules.games.services;

import com.idealizer.review_x.application.modules.games.controllers.dto.FindGamesResponse;

public interface FindManyGamesService {
    FindGamesResponse execute(int limit, int offset, String sortBy, String order);
}
