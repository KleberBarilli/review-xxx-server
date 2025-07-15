package com.idealizer.review_x.modules.games.services;

import com.idealizer.review_x.modules.games.entities.Game;
import com.idealizer.review_x.modules.games.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindManyGamesService {
    private final GameRepository gameRepository;

    public FindManyGamesService (GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> execute() {
        return gameRepository.findAll();
    }
}
