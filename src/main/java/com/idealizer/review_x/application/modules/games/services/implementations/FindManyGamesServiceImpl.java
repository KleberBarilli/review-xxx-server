package com.idealizer.review_x.application.modules.games.services.implementations;

import com.idealizer.review_x.application.modules.games.controllers.dto.FindGamesResponse;
import com.idealizer.review_x.application.modules.games.controllers.dto.GameDTO;
import com.idealizer.review_x.application.modules.games.controllers.dto.SimpleGameDTO;
import com.idealizer.review_x.application.modules.games.entities.Game;
import com.idealizer.review_x.application.modules.games.repositories.GameRepository;
import com.idealizer.review_x.application.modules.games.services.FindManyGamesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindManyGamesServiceImpl implements FindManyGamesService {
    private final GameRepository gameRepository;

    public FindManyGamesServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public FindGamesResponse execute(int limit, int pageNumber, String sort, String order) {

        Sort sortOrder = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();


        Pageable pageable = PageRequest.of(pageNumber, limit, sortOrder);
        Page<Game> games = gameRepository.findAll(pageable);
        List<Game> listOfGames = games.getContent();
        List<SimpleGameDTO> data = listOfGames.stream().map(game -> {
            ;
            SimpleGameDTO simpleGameDTO = new SimpleGameDTO();
            simpleGameDTO.setId(game.getId() != null ? game.getId().toHexString() : null);
            simpleGameDTO.setExternalId(game.getIgdbId());
            simpleGameDTO.setName(game.getName());
            simpleGameDTO.setFirstReleaseDate(game.getFirstReleaseDate());
            simpleGameDTO.setGenres(game.getGenres());
            simpleGameDTO.setModes(game.getModes());
            simpleGameDTO.setPlatforms(game.getPlatforms());
            simpleGameDTO.setRating(game.getTotalRating());
            simpleGameDTO.setRatingCount(game.getTotalRatingCount());
            simpleGameDTO.setCover(game.getCover());
            simpleGameDTO.setUpdatedAt(game.getUpdatedAt());
            return simpleGameDTO;
        }).collect(Collectors.toList());

        FindGamesResponse gamesResponse = new FindGamesResponse();
        gamesResponse.setpageNumber(pageNumber);
        gamesResponse.setLimit(games.getSize());
        gamesResponse.setTotalElements(games.getTotalElements());
        gamesResponse.setTotalPages(games.getTotalPages());
        gamesResponse.setLast(games.isLast());
        gamesResponse.setData(data);

        return gamesResponse;
    }
}
