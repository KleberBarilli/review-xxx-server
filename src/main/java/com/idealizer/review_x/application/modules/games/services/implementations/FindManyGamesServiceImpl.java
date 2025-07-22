package com.idealizer.review_x.application.modules.games.services.implementations;

import com.idealizer.review_x.application.modules.games.controllers.dto.FindGamesResponseDTO;
import com.idealizer.review_x.application.modules.games.controllers.dto.SimpleGameResponseDTO;
import com.idealizer.review_x.application.modules.games.controllers.mappers.GameMapper;
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
    private final GameMapper gameMapper;

    public FindManyGamesServiceImpl(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    @Override
    public FindGamesResponseDTO execute(int limit, int pageNumber, String sort, String order) {

        Sort sortOrder = order.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        Pageable pageable = PageRequest.of(pageNumber, limit, sortOrder);
        Page<Game> games = gameRepository.findAll(pageable);

        List<SimpleGameResponseDTO> data = gameMapper.toSimpleDomainList(games.getContent());

        FindGamesResponseDTO response = new FindGamesResponseDTO();
        response.setpageNumber(pageNumber);
        response.setLimit(games.getSize());
        response.setTotalElements(games.getTotalElements());
        response.setTotalPages(games.getTotalPages());
        response.setLast(games.isLast());
        response.setData(data);

        return response;
    }
}
