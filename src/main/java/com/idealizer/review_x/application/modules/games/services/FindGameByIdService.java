package com.idealizer.review_x.application.modules.games.services;

import com.idealizer.review_x.application.modules.games.controllers.dto.GameResponseDTO;
import com.idealizer.review_x.application.modules.games.entities.Game;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface FindGameByIdService {
    Optional<GameResponseDTO> execute(ObjectId id);
}
