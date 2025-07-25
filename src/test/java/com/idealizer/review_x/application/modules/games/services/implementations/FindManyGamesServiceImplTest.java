package com.idealizer.review_x.application.modules.games.services.implementations;

import com.idealizer.review_x.application.modules.games.entities.Game;
import com.idealizer.review_x.application.modules.games.repositories.GameRepository;
import com.idealizer.review_x.application.modules.games.services.mappers.GameMapper;
import com.idealizer.review_x.application.modules.games.services.outputs.FindGamesResponseDTO;
import com.idealizer.review_x.application.modules.games.services.outputs.SimpleGameResponseDTO;
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
import static org.mockito.Mockito.*;

class FindManyGamesServiceImplTest {

    private GameRepository gameRepository;
    private GameMapper gameMapper;
    private FindManyGamesServiceImpl findManyGamesService;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gameMapper = mock(GameMapper.class);
        findManyGamesService = new FindManyGamesServiceImpl(gameRepository, gameMapper);
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

        SimpleGameResponseDTO simpleGame = new SimpleGameResponseDTO();
        simpleGame.setId(game.getId().toHexString());
        simpleGame.setName(game.getName());
        simpleGame.setUpdatedAt(Instant.now());

        when(gameRepository.findAll(any(Pageable.class))).thenReturn(gamePage);
        when(gameMapper.toSimpleDomainList(List.of(game))).thenReturn(List.of(simpleGame));

        FindGamesResponseDTO result = findManyGamesService.execute(limit, page, sort, order);

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