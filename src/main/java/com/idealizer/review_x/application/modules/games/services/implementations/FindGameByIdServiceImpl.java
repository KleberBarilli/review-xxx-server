package com.idealizer.review_x.application.modules.games.services.implementations;

import com.idealizer.review_x.application.modules.games.services.outputs.GameResponseDTO;
import com.idealizer.review_x.application.modules.games.services.mappers.GameMapper;
import com.idealizer.review_x.application.modules.games.repositories.GameRepository;
import com.idealizer.review_x.application.modules.games.services.FindGameByIdService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindGameByIdServiceImpl implements FindGameByIdService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public FindGameByIdServiceImpl(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    @Override
    public Optional<GameResponseDTO> execute(ObjectId id) {
        return gameRepository.findById(id)
                .map(gameMapper::toDetailedDomain);
    }
}
