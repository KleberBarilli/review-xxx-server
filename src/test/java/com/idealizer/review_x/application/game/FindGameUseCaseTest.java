package com.idealizer.review_x.application.game;

import com.idealizer.review_x.application.games.game.mappers.GameMapper;
import com.idealizer.review_x.application.games.game.responses.FindGameResponse;
import com.idealizer.review_x.application.games.game.responses.SimpleGameResponse;
import com.idealizer.review_x.application.games.game.usecases.FindGameUseCase;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class FindGameUseCaseTest {

    private GameRepository gameRepository;
    private GameMapper gameMapper;
    private FindGameUseCase findGameUseCase;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gameMapper = mock(GameMapper.class);
        findGameUseCase = new FindGameUseCase(gameRepository, gameMapper);
    }

    @Test
    void shouldReturnPaginatedGames() {
        int limit = 10;
        int page = 0;
        String sort = "name";
        String order = "asc";

        Game game = new Game();
        game.setId(new ObjectId());
        game.setName("The Witcher");

        Page<Game> gamePage = new PageImpl<>(List.of(game), PageRequest.of(page, limit), 1);

        SimpleGameResponse simpleGame = new SimpleGameResponse();
        simpleGame.setId(game.getId().toHexString());
        simpleGame.setName(game.getName());
        simpleGame.setUpdatedAt(Instant.now());

        when(gameRepository.findAll(any(Pageable.class))).thenReturn(gamePage);
        when(gameMapper.toSimpleDomainList(List.of(game))).thenReturn(List.of(simpleGame));

        FindGameResponse result = findGameUseCase.execute(limit, page, sort, order);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(simpleGame.getId(), result.getData().get(0).getId());
        assertEquals(limit, result.getLimit());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(page, result.getpageNumber());
        assertTrue(result.isLast());

        verify(gameRepository, times(1)).findAll(any(Pageable.class));
        verify(gameMapper, times(1)).toSimpleDomainList(List.of(game));
    }
}
