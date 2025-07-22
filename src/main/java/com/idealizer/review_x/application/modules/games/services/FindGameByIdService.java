package com.idealizer.review_x.application.modules.games.services;

import com.idealizer.review_x.application.modules.games.entities.Game;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface FindGameByIdService {
    Optional<Game> execute(ObjectId id);
}
