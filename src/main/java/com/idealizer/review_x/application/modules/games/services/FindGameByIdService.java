package com.idealizer.review_x.application.modules.games.services;

import com.idealizer.review_x.application.modules.games.services.outputs.GameResponseDTO;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface FindGameByIdService {
    Optional<GameResponseDTO> execute(ObjectId id);
}
