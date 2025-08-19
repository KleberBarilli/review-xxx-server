package com.idealizer.review_x.application.profile.game;

import com.idealizer.review_x.application.games.profile.game.usecases.FindProfileGameByUsernameUseCase;
import com.idealizer.review_x.common.dtos.PageResponse;
import com.idealizer.review_x.common.dtos.profile.game.FindProfileGamesDTO;
import com.idealizer.review_x.domain.profile.game.interfaces.BaseProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProfileGameByUsernameUseCaseTest {

    @Mock
    private ProfileGameRepository profileGameRepository;

    @InjectMocks
    private FindProfileGameByUsernameUseCase useCase;

    private BaseProfileGame baseProjection;

    @BeforeEach
    void setUp() {
        baseProjection = mock(BaseProfileGame.class);
    }

    @Test
    void shouldReturnPagedResult_forwardParamsAndMapToResponse() {
        String username = "kleber";
        FindProfileGamesDTO dto = new FindProfileGamesDTO(
                0, 10, "updatedAt", "desc",
                null, null, null, null, null, null, null
        );

        Page<BaseProfileGame> repoPage = new PageImpl<>(
                List.of(baseProjection),
                PageRequest.of(0, 10),
                20
        );

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        when(profileGameRepository.searchProjected(eq(username), eq(dto), any(Pageable.class)))
                .thenReturn(repoPage);

        PageResponse<BaseProfileGame> resp = useCase.execute(username, dto);

        assertEquals(0, resp.pageNumber());
        assertEquals(10, resp.limit());
        assertEquals(20, resp.totalElements());
        assertEquals(2, resp.totalPages());
        assertFalse(resp.last());
        assertEquals(1, resp.data().size());

        verify(profileGameRepository).searchProjected(eq(username), eq(dto), pageableCaptor.capture());
        Pageable passed = pageableCaptor.getValue();
        assertEquals(0, passed.getPageNumber());
        assertEquals(10, passed.getPageSize());
        assertTrue(passed.getSort().isUnsorted()); // <- aqui está o ponto
    }

    @Test
    void shouldClampPageAndLimit_whenNegativeOrZero() {
        String username = "kleber";
        FindProfileGamesDTO dto = new FindProfileGamesDTO(
                -5, 0, null, null,
                Set.of(), null, null, null, null, null, null
        );

        when(profileGameRepository.searchProjected(eq(username), eq(dto), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 1), 0));

        PageResponse<BaseProfileGame> resp = useCase.execute(username, dto);

        assertEquals(0, resp.pageNumber());
        assertEquals(1, resp.limit());
        assertEquals(0, resp.totalElements());
        assertEquals(0, resp.totalPages());
        assertTrue(resp.last());
        assertTrue(resp.data().isEmpty());
    }
}
