package com.idealizer.review_x.application.game.usecases;

import com.idealizer.review_x.application.game.mappers.GameMapper;
import com.idealizer.review_x.application.game.responses.GameResponse;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindGameByIdUseCase {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public FindGameByIdUseCase(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    public Optional<GameResponse> execute(ObjectId id) {
        return gameRepository.findById(id)
                .map(gameMapper::toDetailedDomain);
    }

}
