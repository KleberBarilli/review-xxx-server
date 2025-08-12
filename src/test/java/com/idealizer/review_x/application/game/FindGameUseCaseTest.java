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
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class FindGameUseCaseTest {

    private GameRepository gameRepository;
    private GameMapper gameMapper;
    private FindGameUseCase useCase;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gameMapper = mock(GameMapper.class);
        useCase = new FindGameUseCase(gameRepository, gameMapper);
    }

    @Test
    void shouldReturnPaginatedGames_whenNoSlug() {
        int limit = 10, page = 0;
        String sort = "name", order = "asc";
        String slug = null;

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

        FindGameResponse result = useCase.execute(limit, page, sort, order, slug);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(simple.getId(), result.getData().get(0).getId());
        assertEquals(limit, result.getLimit());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(page, result.getpageNumber());
        assertTrue(result.isLast());

        verify(gameRepository, times(1)).findAll(any(Pageable.class));
        verify(gameRepository, never()).findBySlugRegex(anyString(), any(Pageable.class));
        verify(gameMapper, times(1)).toSimpleDomainList(List.of(game));
    }

    @Test
    void shouldQueryWithAnchoredPrefixRegex_whenSlugProvided() {
        int limit = 10, page = 0;
        String sort = "total_rating_count", order = "desc";
        String slug = "grand-theft-auto"; // deve trazer todos os GTA (exato e com sufixos)

        Game gtaV = new Game();
        gtaV.setId(new ObjectId());
        gtaV.setName("Grand Theft Auto V");
        gtaV.setSlug("grand-theft-auto-v");

        Game gtaIV = new Game();
        gtaIV.setId(new ObjectId());
        gtaIV.setName("Grand Theft Auto IV");
        gtaIV.setSlug("grand-theft-auto-iv");

        Page<Game> regexPage = new PageImpl<>(List.of(gtaV, gtaIV), PageRequest.of(page, limit), 2);

        String expectedRegex = "^" + java.util.regex.Pattern.quote(slug) + "(?:-|$)";
        when(gameRepository.findBySlugRegex(eq(expectedRegex), any(Pageable.class))).thenReturn(regexPage);

        SimpleGameResponse d1 = new SimpleGameResponse();
        d1.setId(gtaV.getId().toHexString());
        d1.setName(gtaV.getName());
        d1.setUpdatedAt(Instant.now());

        SimpleGameResponse d2 = new SimpleGameResponse();
        d2.setId(gtaIV.getId().toHexString());
        d2.setName(gtaIV.getName());
        d2.setUpdatedAt(Instant.now());

        when(gameMapper.toSimpleDomainList(List.of(gtaV, gtaIV))).thenReturn(List.of(d1, d2));

        FindGameResponse result = useCase.execute(limit, page, sort, order, slug);

        assertNotNull(result);
        assertEquals(2, result.getData().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(page, result.getpageNumber());
        assertTrue(result.isLast());

        // Verifica regex e sort aplicado
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(gameRepository, times(1)).findBySlugRegex(eq(expectedRegex), pageableCaptor.capture());
        Pageable usedPageable = pageableCaptor.getValue();
        Sort sortUsed = usedPageable.getSort();
        assertEquals(Sort.Direction.DESC, sortUsed.getOrderFor("total_rating_count").getDirection());
        assertNotNull(sortUsed.getOrderFor("_id")); // sort estável inclui _id

        verify(gameRepository, never()).findAll(any(Pageable.class));
        verify(gameMapper, times(1)).toSimpleDomainList(List.of(gtaV, gtaIV));
    }

    @Test
    void shouldNormalizeAndUseRegex_whenSlugHasSpacesAndUppercase() {
        int limit = 10, page = 0;
        String sort = "updatedAt", order = "asc";
        String rawSlug = "Grand Theft Auto";        // será normalizado para "grand-theft-auto"
        String normalized = "grand-theft-auto";
        String expectedRegex = "^" + java.util.regex.Pattern.quote(normalized) + "(?:-|$)";

        Game gtaSA = new Game();
        gtaSA.setId(new ObjectId());
        gtaSA.setName("Grand Theft Auto: San Andreas");
        gtaSA.setSlug("grand-theft-auto-san-andreas");

        Page<Game> regexPage = new PageImpl<>(List.of(gtaSA), PageRequest.of(page, limit), 1);
        when(gameRepository.findBySlugRegex(eq(expectedRegex), any(Pageable.class))).thenReturn(regexPage);

        SimpleGameResponse dto = new SimpleGameResponse();
        dto.setId(gtaSA.getId().toHexString());
        dto.setName(gtaSA.getName());
        dto.setUpdatedAt(Instant.now());
        when(gameMapper.toSimpleDomainList(List.of(gtaSA))).thenReturn(List.of(dto));

        FindGameResponse result = useCase.execute(limit, page, sort, order, rawSlug);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(page, result.getpageNumber());
        assertTrue(result.isLast());

        verify(gameRepository, times(1)).findBySlugRegex(eq(expectedRegex), any(Pageable.class));
        verify(gameRepository, never()).findAll(any(Pageable.class));
        verify(gameMapper, times(1)).toSimpleDomainList(List.of(gtaSA));
    }
}