package com.idealizer.review_x.application.game;

import com.idealizer.review_x.application.game.mappers.GameMapper;
import com.idealizer.review_x.application.game.responses.GameResponse;
import com.idealizer.review_x.application.game.usecases.FindGameByIdUseCase;
import com.idealizer.review_x.domain.games.entities.Game;
import com.idealizer.review_x.domain.games.repositories.GameRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FindGameByIdServiceImplTest {

    private GameRepository gameRepository;
    private GameMapper gameMapper;
    private FindGameByIdUseCase findGameByIdUseCase;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gameMapper = mock(GameMapper.class);
        findGameByIdUseCase = new FindGameByIdUseCase(gameRepository, gameMapper);
    }

    @Test
    void shouldReturnMappedGameResponseDTOWhenGameExists() {
        ObjectId id = new ObjectId();
        Game game = new Game();
        game.setId(id);

        GameResponse dto = new GameResponse();
        dto.setId(id.toHexString());

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(gameMapper.toDetailedDomain(game)).thenReturn(dto);

        Optional<GameResponse> result = findGameByIdUseCase.execute(id);

        assertTrue(result.isPresent());
        assertEquals(id.toHexString(), result.get().getId());

        verify(gameRepository, times(1)).findById(id);
        verify(gameMapper, times(1)).toDetailedDomain(game);
    }

    @Test
    void shouldReturnEmptyWhenGameDoesNotExist() {

        ObjectId id = new ObjectId();
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        Optional<GameResponse> result = findGameByIdUseCase.execute(id);

        assertTrue(result.isEmpty());
        verify(gameRepository, times(1)).findById(id);
        verify(gameMapper, never()).toDetailedDomain(any());
    }
}
