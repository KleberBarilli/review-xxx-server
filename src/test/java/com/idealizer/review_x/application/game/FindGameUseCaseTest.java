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
    void shouldReturnPaginatedGames_whenNoSlug() {
        int limit = 10;
        int page = 0;
        String sort = "name";
        String order = "asc";
        String slug = null; // no slug path

        Game game = new Game();
        game.setId(new ObjectId());
        game.setName("The Witcher");

        Page<Game> gamePage = new PageImpl<>(List.of(game), PageRequest.of(page, limit), 1);

        SimpleGameResponse simple = new SimpleGameResponse();
        simple.setId(game.getId().toHexString());
        simple.setName(game.getName());
        simple.setUpdatedAt(Instant.now());

        when(gameRepository.findAll(any(Pageable.class))).thenReturn(gamePage);
        when(gameMapper.toSimpleDomainList(List.of(game))).thenReturn(List.of(simple));

        FindGameResponse result = findGameUseCase.execute(limit, page, sort, order, slug);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(simple.getId(), result.getData().get(0).getId());
        assertEquals(limit, result.getLimit());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(page, result.getpageNumber());
        assertTrue(result.isLast());

        verify(gameRepository, times(1)).findAll(any(Pageable.class));
        verify(gameRepository, never()).autocompleteSlug(anyString(), anyInt(), anyInt());
        verify(gameRepository, never()).autocompleteSlugCount(anyString());
        verify(gameMapper, times(1)).toSimpleDomainList(List.of(game));
    }

    @Test
    void shouldReturnPaginatedGames_whenSlugProvided() {
        int limit = 10;
        int page = 0;
        String sort = "name";
        String order = "asc";
        String slug = "witcher"; // triggers Atlas Search path

        Game game1 = new Game();
        game1.setId(new ObjectId());
        game1.setName("The Witcher");

        Game game2 = new Game();
        game2.setId(new ObjectId());
        game2.setName("The Witcher 2: Assassins of Kings");

        List<Game> searchHits = List.of(game1, game2);

        SimpleGameResponse s1 = new SimpleGameResponse();
        s1.setId(game1.getId().toHexString());
        s1.setName(game1.getName());
        s1.setUpdatedAt(Instant.now());

        SimpleGameResponse s2 = new SimpleGameResponse();
        s2.setId(game2.getId().toHexString());
        s2.setName(game2.getName());
        s2.setUpdatedAt(Instant.now());

        // total from $searchMeta
        GameRepository.TotalOnly totalOnly = () -> 124L;

        when(gameRepository.autocompleteSlug(eq(slug), eq(page * limit), eq(limit))).thenReturn(searchHits);
        when(gameRepository.autocompleteSlugCount(eq(slug))).thenReturn(List.of(totalOnly));
        when(gameMapper.toSimpleDomainList(searchHits)).thenReturn(List.of(s1, s2));

        FindGameResponse result = findGameUseCase.execute(limit, page, sort, order, slug);

        assertNotNull(result);
        assertEquals(2, result.getData().size());
        assertEquals(124L, result.getTotalElements());
        assertEquals((int) Math.ceil(124.0 / limit), result.getTotalPages());
        assertEquals(page, result.getpageNumber());
        assertFalse(result.isLast()); // since total >> page

        verify(gameRepository, times(1)).autocompleteSlug(eq(slug), eq(0), eq(limit));
        verify(gameRepository, times(1)).autocompleteSlugCount(eq(slug));
        verify(gameRepository, never()).findAll(any(Pageable.class));
        verify(gameMapper, times(1)).toSimpleDomainList(searchHits);
    }
}