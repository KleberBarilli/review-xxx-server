package com.idealizer.review_x.application.game;

import com.idealizer.review_x.application.games.game.mappers.GameMapper;
import com.idealizer.review_x.application.games.game.responses.FindGameResponse;
import com.idealizer.review_x.application.games.game.responses.SimpleGameResponse;
import com.idealizer.review_x.application.games.game.usecases.FindGameUseCase;
import com.idealizer.review_x.common.dtos.FindAllGamesDTO;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.entities.enums.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FindGameUseCaseTest {

    private MongoTemplate mongoTemplate;
    private GameMapper gameMapper;
    private FindGameUseCase useCase;

    @BeforeEach
    void setUp() {
        mongoTemplate = mock(MongoTemplate.class);
        gameMapper = mock(GameMapper.class);
        useCase = new FindGameUseCase(mongoTemplate, gameMapper);
    }

    @Test
    void shouldReturnPagedResult_withoutFilters_usesDefaultsAndStableSort() {
        int limit = 10, page = 1;
        String sort = null;
        String order = null;

        FindAllGamesDTO dto = new FindAllGamesDTO(
                limit, page, sort, order,
                null,
                null,
                null,
                null,
                null,
                null,
                null  , null, null
        );

        Game g1 = new Game(); g1.setId(new ObjectId()); g1.setName("A");
        Game g2 = new Game(); g2.setId(new ObjectId()); g2.setName("B");
        List<Game> found = List.of(g1, g2);

        ArgumentCaptor<Query> findQ = ArgumentCaptor.forClass(Query.class);
        ArgumentCaptor<Query> countQ = ArgumentCaptor.forClass(Query.class);

        when(mongoTemplate.find(findQ.capture(), eq(Game.class), eq("games"))).thenReturn(found);
        when(mongoTemplate.count(countQ.capture(), eq(Game.class), eq("games"))).thenReturn(42L);

        SimpleGameResponse s1 = new SimpleGameResponse(); s1.setId(g1.getId().toHexString()); s1.setName("A");
        SimpleGameResponse s2 = new SimpleGameResponse(); s2.setId(g2.getId().toHexString()); s2.setName("B");
        when(gameMapper.toSimpleDomainList(found)).thenReturn(List.of(s1, s2));


        FindGameResponse resp = useCase.execute(dto);

        assertNotNull(resp);
        assertEquals(page, resp.getpageNumber());
        assertEquals(limit, resp.getLimit());
        assertEquals(42L, resp.getTotalElements());
        assertEquals((int) Math.ceil(42.0 / limit), resp.getTotalPages());
        assertFalse(resp.isLast());
        assertEquals(2, resp.getData().size());

        Query q = findQ.getValue();
        assertEquals(page * limit, q.getSkip());
        assertEquals(limit, q.getLimit());

        Document sortDoc = q.getSortObject();
        assertEquals(-1, sortDoc.get("total_rating_count"));
        assertEquals(-1, sortDoc.get("_id"));

        Document qo = q.getQueryObject();
        assertTrue(qo.isEmpty());

        verify(mongoTemplate, times(1)).count(any(Query.class), eq(Game.class), eq("games"));
    }

    @Test
    void shouldApplySlugPrefixAndDeveloperAndTypeAndEngines_andRespectCustomSortAsc() {
        int limit = 5, page = 0;
        String sort = "updatedAt", order = "asc";

        String rawSlug = "Grand  Theft!! Auto";
        String normalized = "grand-theft-auto";

        FindAllGamesDTO dto = new FindAllGamesDTO(
                limit, page, sort, order,
                rawSlug,
                "Blizzard Entertainment",
                List.of(GameStatus.BETA),
                List.of(GameGenre.RACING),
                List.of(GameMode.SINGLE_PLAYER),
                List.of(GamePlatform.GAME_BOY),
                LocalDate.of(2015,1,1),
                List.of(GameType.REMAKE, GameType.MAIN_GAME),
                List.of("Unity", "Unreal")

        );

        Game g1 = new Game(); g1.setId(new ObjectId()); g1.setName("GTA V");
        List<Game> found = List.of(g1);

        ArgumentCaptor<Query> findQ = ArgumentCaptor.forClass(Query.class);
        when(mongoTemplate.find(findQ.capture(), eq(Game.class), eq("games"))).thenReturn(found);
        when(mongoTemplate.count(any(Query.class), eq(Game.class), eq("games"))).thenReturn(1L);

        SimpleGameResponse s1 = new SimpleGameResponse(); s1.setId(g1.getId().toHexString()); s1.setName("GTA V");
        when(gameMapper.toSimpleDomainList(found)).thenReturn(List.of(s1));

        FindGameResponse resp = useCase.execute(dto);

        assertNotNull(resp);
        assertEquals(1, resp.getTotalElements());
        assertEquals(1, resp.getTotalPages());
        assertTrue(resp.isLast());
        assertEquals(1, resp.getData().size());

        Query q = findQ.getValue();

        Document sortDoc = q.getSortObject();
        assertEquals(1, sortDoc.get("updatedAt"));
        assertEquals(-1, sortDoc.get("_id"));

        Document qo = q.getQueryObject();

        assertTrue(qo.containsKey("slug"), "slug prefix criterion missing");
        Document slugDoc = (Document) qo.get("slug");
        assertEquals(normalized, slugDoc.getString("$gte"));
        assertEquals(normalized + "\uffff", slugDoc.getString("$lt"));

        assertTrue(qo.containsKey("developer"), "developer criterion missing");
        assertTrue(qo.containsKey("status"), "status criterion missing");

        assertTrue(qo.containsKey("genres"));
        assertTrue(((Document) qo.get("genres")).containsKey("$in"));

        assertTrue(qo.containsKey("modes"));
        assertTrue(((Document) qo.get("modes")).containsKey("$in"));

        assertTrue(qo.containsKey("platforms"));
        assertTrue(((Document) qo.get("platforms")).containsKey("$in"));

        assertTrue(qo.containsKey("type"));
        assertTrue(((Document) qo.get("type")).containsKey("$in"));

        assertTrue(qo.containsKey("engines"));
        assertTrue(((Document) qo.get("engines")).containsKey("$in"));

        assertTrue(qo.containsKey("firstReleaseDate"));
        assertTrue(((Document) qo.get("firstReleaseDate")).containsKey("$gte"));

        assertEquals(0, q.getSkip());
        assertEquals(limit, q.getLimit());
    }

    @Test
    void shouldNotBreak_whenAllFiltersNullOrEmpty_andUseCapOnLimit() {
        FindAllGamesDTO dto = new FindAllGamesDTO(
                0, -1, null, "desc",
                null, null, List.of(),List.of(), List.of(), List.of(),
                null, List.of(), List.of()
        );

        List<Game> found = List.of();
        ArgumentCaptor<Query> findQ = ArgumentCaptor.forClass(Query.class);

        when(mongoTemplate.find(findQ.capture(), eq(Game.class), eq("games"))).thenReturn(found);
        when(mongoTemplate.count(any(Query.class), eq(Game.class), eq("games"))).thenReturn(0L);
        when(gameMapper.toSimpleDomainList(found)).thenReturn(List.of());

        FindGameResponse resp = useCase.execute(dto);

        assertNotNull(resp);
        assertEquals(0, resp.getTotalElements());
        assertEquals(0, resp.getTotalPages());
        assertTrue(resp.isLast());
        assertEquals(0, resp.getData().size());

        Query q = findQ.getValue();
        assertEquals(0, q.getSkip());
        assertEquals(20, q.getLimit());

        Document sortDoc = q.getSortObject();
        assertEquals(-1, sortDoc.get("total_rating_count"));
        assertEquals(-1, sortDoc.get("_id"));

        assertTrue(q.getQueryObject().isEmpty());
    }
}