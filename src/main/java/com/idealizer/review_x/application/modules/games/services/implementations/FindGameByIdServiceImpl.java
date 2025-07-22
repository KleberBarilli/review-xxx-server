package com.idealizer.review_x.application.modules.games.services.implementations;

import com.idealizer.review_x.application.modules.games.controllers.dto.GameResponseDTO;
import com.idealizer.review_x.application.modules.games.entities.Game;
import com.idealizer.review_x.application.modules.games.repositories.GameRepository;
import com.idealizer.review_x.application.modules.games.services.FindGameByIdService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindGameByIdServiceImpl implements FindGameByIdService {

    private final GameRepository gameRepository;

    public FindGameByIdServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Optional<Game> execute(ObjectId id) {
        return gameRepository.findById(id);

    }
}
