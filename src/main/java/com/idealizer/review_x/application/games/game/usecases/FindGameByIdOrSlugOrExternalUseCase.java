package com.idealizer.review_x.application.games.game.usecases;

import com.idealizer.review_x.application.games.game.mappers.GameMapper;
import com.idealizer.review_x.application.games.game.responses.GameResponse;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindGameByIdOrSlugOrExternalUseCase {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public FindGameByIdOrSlugOrExternalUseCase(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    public Optional<GameResponse> execute(ObjectId id, String slug, Integer externalId) {
        if( id != null && !id.toHexString().isEmpty()) {
            return gameRepository.findById(id)
                    .map(gameMapper::toDetailedDomain);
        }

    if (slug != null && !slug.isBlank()) {
            return gameRepository.findBySlug(slug)
                    .map(gameMapper::toDetailedDomain);
        }
    if (externalId != null) {
            return gameRepository.findByIgdbId(externalId)
                    .map(gameMapper::toDetailedDomain);
        }

       return Optional.empty();
    }

}
