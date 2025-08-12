package com.idealizer.review_x.application.game;

import com.idealizer.review_x.application.games.game.mappers.GameMapper;
import com.idealizer.review_x.application.games.game.responses.FindGameResponse;
import com.idealizer.review_x.application.games.game.responses.SimpleGameResponse;
import com.idealizer.review_x.application.games.game.usecases.FindGameUseCase;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;



@ExtendWith(MockitoExtension.class)
class FindGameUseCaseTest {

    private static final String HI_SENTINEL = "\uffff";

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private FindGameUseCase useCase;

    private static SimpleGameResponse dtoFrom(Game g) {
        SimpleGameResponse dto = new SimpleGameResponse();
        dto.setId(g.getId().toHexString());
        dto.setName(g.getName());
        dto.setUpdatedAt(Instant.now());
        return dto;
    }

    @Test
    void shouldReturnPaginatedGames_whenNoSlug() {
        // given
        int limit = 10, page = 0;
        String sort = "name", order = "asc";
        String slug = null;

        Game game = new Game();
        game.setId(new ObjectId());
        game.setName("The Witcher");

        Page<Game> gamePage = new PageImpl<>(List.of(game), PageRequest.of(page, limit), 1);

        when(gameRepository.findAll(any(Pageable.class))).thenReturn(gamePage);
        when(gameMapper.toSimpleDomainList(anyList())).thenReturn(List.of(dtoFrom(game)));

        // when
        FindGameResponse result = useCase.execute(limit, page, sort, order, slug);

        // then
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(limit, result.getLimit());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(page, result.getpageNumber());
        assertTrue(result.isLast());

        // verify pageable: sort asc by 'name' plus stable _id desc
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(gameRepository, times(1)).findAll(pageableCaptor.capture());
        Pageable used = pageableCaptor.getValue();
        Sort s = used.getSort();
        assertEquals(Sort.Direction.ASC, s.getOrderFor("name").getDirection());
        assertEquals(Sort.Direction.DESC, s.getOrderFor("_id").getDirection());

        verify(gameRepository, never()).findBySlugPrefix(anyString(), anyString(), any(Pageable.class));
        verify(gameMapper, times(1)).toSimpleDomainList(anyList());
    }

    @Test
    void shouldSearchByPrefix_whenSlugProvided() {
        // given
        int limit = 10, page = 0;
        String sort = "total_rating_count", order = "desc";
        String slug = "grand-theft-auto"; // exact/prefix

        Game gtaV = new Game();
        gtaV.setId(new ObjectId());
        gtaV.setName("Grand Theft Auto V");
        gtaV.setSlug("grand-theft-auto-v");

        Game gtaIV = new Game();
        gtaIV.setId(new ObjectId());
        gtaIV.setName("Grand Theft Auto IV");
        gtaIV.setSlug("grand-theft-auto-iv");

        Page<Game> pageResult = new PageImpl<>(List.of(gtaV, gtaIV), PageRequest.of(page, limit), 2);

        when(gameRepository.findBySlugPrefix(eq(slug), eq(slug + HI_SENTINEL), any(Pageable.class)))
                .thenReturn(pageResult);
        when(gameMapper.toSimpleDomainList(anyList())).thenReturn(List.of(dtoFrom(gtaV), dtoFrom(gtaIV)));

        // when
        FindGameResponse result = useCase.execute(limit, page, sort, order, slug);

        // then
        assertNotNull(result);
        assertEquals(2, result.getData().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());

        // verify lo/hi bounds and sort
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(gameRepository).findBySlugPrefix(eq(slug), eq(slug + HI_SENTINEL), pageableCaptor.capture());
        Sort s = pageableCaptor.getValue().getSort();
        assertEquals(Sort.Direction.DESC, s.getOrderFor("total_rating_count").getDirection());
        assertEquals(Sort.Direction.DESC, s.getOrderFor("_id").getDirection());

        verify(gameRepository, never()).findAll(any(Pageable.class));
    }

    @ParameterizedTest
    @CsvSource({
            "'Grand Theft Auto',grand-theft-auto",
            "'  Grand   Theft   Auto ',grand-theft-auto",
            "'Grand-Theft_Auto!!',grand-theft-auto",
            "'-Grand--Theft--Auto-',grand-theft-auto"
    })
    void shouldNormalizeInputAndSearchWithPrefixRange(String raw, String expectedNormalized) {
        // given
        int limit = 10, page = 0;
        String sort = "updatedAt", order = "asc";

        Game gtaSA = new Game();
        gtaSA.setId(new ObjectId());
        gtaSA.setName("Grand Theft Auto: San Andreas");
        gtaSA.setSlug("grand-theft-auto-san-andreas");

        Page<Game> pageResult = new PageImpl<>(List.of(gtaSA), PageRequest.of(page, limit), 1);

        when(gameRepository.findBySlugPrefix(eq(expectedNormalized), eq(expectedNormalized + HI_SENTINEL), any(Pageable.class)))
                .thenReturn(pageResult);
        when(gameMapper.toSimpleDomainList(anyList())).thenReturn(List.of(dtoFrom(gtaSA)));

        // when
        FindGameResponse result = useCase.execute(limit, page, sort, order, raw);

        // then
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());

        // verify sort stability: updatedAt asc + _id desc
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(gameRepository).findBySlugPrefix(eq(expectedNormalized), eq(expectedNormalized + HI_SENTINEL), pageableCaptor.capture());
        Sort s = pageableCaptor.getValue().getSort();
        assertEquals(Sort.Direction.ASC, s.getOrderFor("updatedAt").getDirection());
        assertEquals(Sort.Direction.DESC, s.getOrderFor("_id").getDirection());

        verify(gameRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void shouldFallbackToDefaults_whenInvalidPagingAndSorting() {
        // given: limit <= 0 -> default 20; page < 0 -> default 0; sort blank -> default total_rating_count desc
        int limit = 0, page = -5;
        String sort = "  ", order = "whatever";
        String slug = null;

        Game g = new Game();
        g.setId(new ObjectId());
        g.setName("Test");

        Page<Game> gamePage = new PageImpl<>(List.of(g), PageRequest.of(0, 20), 1);
        when(gameRepository.findAll(any(Pageable.class))).thenReturn(gamePage);
        when(gameMapper.toSimpleDomainList(anyList())).thenReturn(List.of(dtoFrom(g)));

        // when
        FindGameResponse result = useCase.execute(limit, page, sort, order, slug);

        // then
        assertEquals(20, result.getLimit());
        assertEquals(0, result.getpageNumber());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(gameRepository).findAll(pageableCaptor.capture());
        Pageable used = pageableCaptor.getValue();
        assertEquals(0, used.getPageNumber());
        assertEquals(20, used.getPageSize());
        Sort s = used.getSort();
        assertEquals(Sort.Direction.DESC, s.getOrderFor("total_rating_count").getDirection());
        assertEquals(Sort.Direction.DESC, s.getOrderFor("_id").getDirection());
    }
}
