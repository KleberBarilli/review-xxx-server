
package com.idealizer.review_x.application.modules.games.services.implementations;
import com.idealizer.review_x.application.modules.games.entities.Game;
import com.idealizer.review_x.application.modules.games.repositories.GameRepository;
import com.idealizer.review_x.application.modules.games.services.mappers.GameMapper;
import com.idealizer.review_x.application.modules.games.services.outputs.GameResponseDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindGameByIdServiceImplTest {

    private GameRepository gameRepository;
    private GameMapper gameMapper;
    private FindGameByIdServiceImpl findGameByIdService;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gameMapper = mock(GameMapper.class);
        findGameByIdService = new FindGameByIdServiceImpl(gameRepository, gameMapper);
    }

    @Test
    void shouldReturnMappedGameResponseDTOWhenGameExists() {
        ObjectId id = new ObjectId();
        Game game = new Game();
        game.setId(id);

        GameResponseDTO dto = new GameResponseDTO();
        dto.setId(id.toHexString());

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(gameMapper.toDetailedDomain(game)).thenReturn(dto);

        Optional<GameResponseDTO> result = findGameByIdService.execute(id);

        assertTrue(result.isPresent());
        assertEquals(id.toHexString(), result.get().getId());

        verify(gameRepository, times(1)).findById(id);
        verify(gameMapper, times(1)).toDetailedDomain(game);
    }

    @Test
    void shouldReturnEmptyWhenGameDoesNotExist() {

        ObjectId id = new ObjectId();
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        Optional<GameResponseDTO> result = findGameByIdService.execute(id);

        assertTrue(result.isEmpty());
        verify(gameRepository, times(1)).findById(id);
        verify(gameMapper, never()).toDetailedDomain(any());
    }
}
