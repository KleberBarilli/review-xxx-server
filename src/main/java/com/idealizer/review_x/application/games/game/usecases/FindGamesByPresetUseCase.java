package com.idealizer.review_x.application.games.game.usecases;
import com.idealizer.review_x.common.dtos.game.DiscoverPreset;
import com.idealizer.review_x.domain.game.entities.GameRanking;
import com.idealizer.review_x.domain.game.repositories.GameDiscoverRankingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindGamesByPresetUseCase {

    private final GameDiscoverRankingRepository gameDiscoverRankingRepository;

    public FindGamesByPresetUseCase (GameDiscoverRankingRepository gameDiscoverRankingRepository){
        this.gameDiscoverRankingRepository = gameDiscoverRankingRepository;
    }
    public List<GameRanking> execute(DiscoverPreset preset){
        return this.gameDiscoverRankingRepository.findAllByPreset(preset);
    }
}
