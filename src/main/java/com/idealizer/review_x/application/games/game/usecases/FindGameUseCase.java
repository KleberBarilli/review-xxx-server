package com.idealizer.review_x.application.games.game.usecases;

import com.idealizer.review_x.application.games.game.mappers.GameMapper;
import com.idealizer.review_x.application.games.game.responses.FindGameResponse;
import com.idealizer.review_x.application.games.game.responses.SimpleGameResponse;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindGameUseCase {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public FindGameUseCase(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    public FindGameResponse execute(int limit, int pageNumber, String sort, String order) {

        Sort sortOrder = order.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        Pageable pageable = PageRequest.of(pageNumber, limit, sortOrder);
        Page<Game> games = gameRepository.findAll(pageable);

        List<SimpleGameResponse> data = gameMapper.toSimpleDomainList(games.getContent());

        FindGameResponse response = new FindGameResponse();
        response.setpageNumber(pageNumber);
        response.setLimit(games.getSize());
        response.setTotalElements(games.getTotalElements());
        response.setTotalPages(games.getTotalPages());
        response.setLast(games.isLast());
        response.setData(data);

        return response;
    }
}
