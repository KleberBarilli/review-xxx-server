package com.idealizer.review_x.application.game;

import com.idealizer.review_x.application.games.game.mappers.GameMapper;
import com.idealizer.review_x.application.games.game.responses.GameResponse;
import com.idealizer.review_x.application.games.game.usecases.FindGameByIdOrSlugOrExternalUseCase;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FindGameByIdOrSlugOrExternalUseCaseTest {

    private GameRepository gameRepository;
    private GameMapper gameMapper;
    private FindGameByIdOrSlugOrExternalUseCase useCase;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gameMapper = mock(GameMapper.class);
        useCase = new FindGameByIdOrSlugOrExternalUseCase(gameRepository, gameMapper);
    }

    @Test
    void shouldReturnMappedGame_whenFoundById() {
        ObjectId id = new ObjectId();
        Game game = new Game();
        game.setId(id);

        GameResponse dto = new GameResponse();
        dto.setId(id.toHexString());

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(gameMapper.toDetailedDomain(game)).thenReturn(dto);

        Optional<GameResponse> result = useCase.execute(id, null, null);

        assertTrue(result.isPresent());
        assertEquals(id.toHexString(), result.get().getId());

        verify(gameRepository, times(1)).findById(id);
        verify(gameRepository, never()).findBySlug(anyString());
        verify(gameRepository, never()).findByIgdbId(anyInt());
        verify(gameMapper, times(1)).toDetailedDomain(game);
    }

    @Test
    void shouldReturnEmpty_whenNotFoundById() {
        ObjectId id = new ObjectId();

        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        Optional<GameResponse> result = useCase.execute(id, null, null);

        assertTrue(result.isEmpty());
        verify(gameRepository, times(1)).findById(id);
        verify(gameRepository, never()).findBySlug(anyString());
        verify(gameRepository, never()).findByIgdbId(anyInt());
        verify(gameMapper, never()).toDetailedDomain(any());
    }

    @Test
    void shouldReturnMappedGame_whenFoundBySlug_andIdIsNull() {
        String slug = "the-witcher-3-wild-hunt";
        Game game = new Game();
        game.setId(new ObjectId());

        GameResponse dto = new GameResponse();
        dto.setId(game.getId().toHexString());

        when(gameRepository.findBySlug(slug)).thenReturn(Optional.of(game));
        when(gameMapper.toDetailedDomain(game)).thenReturn(dto);

        Optional<GameResponse> result = useCase.execute(null, slug, null);

        assertTrue(result.isPresent());
        assertEquals(dto.getId(), result.get().getId());

        verify(gameRepository, times(1)).findBySlug(slug);
        verify(gameRepository, never()).findById(any());
        verify(gameRepository, never()).findByIgdbId(anyInt());
        verify(gameMapper, times(1)).toDetailedDomain(game);
    }

    @Test
    void shouldReturnMappedGame_whenFoundByExternalId_andIdAndSlugAreNull() {
        Integer externalId = 1020;
        Game game = new Game();
        game.setId(new ObjectId());

        GameResponse dto = new GameResponse();
        dto.setId(game.getId().toHexString());

        when(gameRepository.findByIgdbId(externalId)).thenReturn(Optional.of(game));
        when(gameMapper.toDetailedDomain(game)).thenReturn(dto);

        Optional<GameResponse> result = useCase.execute(null, null, externalId);

        assertTrue(result.isPresent());
        assertEquals(dto.getId(), result.get().getId());

        verify(gameRepository, times(1)).findByIgdbId(externalId);
        verify(gameRepository, never()).findById(any());
        verify(gameRepository, never()).findBySlug(anyString());
        verify(gameMapper, times(1)).toDetailedDomain(game);
    }

    @Test
    void shouldPreferId_overSlugAndExternalId_whenAllProvided() {
        ObjectId id = new ObjectId();
        String slug = "some-slug";
        Integer externalId = 123;

        Game game = new Game();
        game.setId(id);

        GameResponse dto = new GameResponse();
        dto.setId(id.toHexString());

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(gameMapper.toDetailedDomain(game)).thenReturn(dto);

        Optional<GameResponse> result = useCase.execute(id, slug, externalId);

        assertTrue(result.isPresent());
        assertEquals(id.toHexString(), result.get().getId());

        verify(gameRepository, times(1)).findById(id);
        verify(gameRepository, never()).findBySlug(anyString());
        verify(gameRepository, never()).findByIgdbId(anyInt());
        verify(gameMapper, times(1)).toDetailedDomain(game);
    }

    @Test
    void shouldPreferSlug_overExternalId_whenIdIsNull_andBothProvided() {
        String slug = "some-slug";
        Integer externalId = 123;

        Game game = new Game();
        game.setId(new ObjectId());

        GameResponse dto = new GameResponse();
        dto.setId(game.getId().toHexString());

        when(gameRepository.findBySlug(slug)).thenReturn(Optional.of(game));
        when(gameMapper.toDetailedDomain(game)).thenReturn(dto);

        Optional<GameResponse> result = useCase.execute(null, slug, externalId);

        assertTrue(result.isPresent());
        assertEquals(dto.getId(), result.get().getId());

        verify(gameRepository, times(1)).findBySlug(slug);
        verify(gameRepository, never()).findById(any());
        verify(gameRepository, never()).findByIgdbId(anyInt());
        verify(gameMapper, times(1)).toDetailedDomain(game);
    }

    @Test
    void shouldReturnEmpty_whenAllParamsNullOrBlank() {
        Optional<GameResponse> result1 = useCase.execute(null, null, null);

        assertTrue(result1.isEmpty());

        verify(gameRepository, never()).findById(any());
        verify(gameRepository, never()).findBySlug(anyString());
        verify(gameRepository, never()).findByIgdbId(anyInt());
        verify(gameMapper, never()).toDetailedDomain(any());
    }
}