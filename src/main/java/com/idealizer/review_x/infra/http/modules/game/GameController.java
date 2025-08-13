package com.idealizer.review_x.infra.http.modules.game;

import com.idealizer.review_x.application.games.game.responses.FindGameResponse;
import com.idealizer.review_x.application.games.game.responses.GameResponse;
import com.idealizer.review_x.application.games.game.usecases.FindGameByIdOrSlugOrExternalUseCase;
import com.idealizer.review_x.application.games.game.usecases.FindGameUseCase;
import com.idealizer.review_x.common.dtos.FindAllGamesDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/public/games")
@Tag(name = "Games")
public class GameController {

    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    private final FindGameUseCase findGameUseCase;
    private final FindGameByIdOrSlugOrExternalUseCase findGameByIdOrSlugOrExternalUseCase;

    public GameController(FindGameUseCase findGameUseCase, FindGameByIdOrSlugOrExternalUseCase findGameByIdOrSlugOrExternalUseCase
                       ) {
        this.findGameUseCase = findGameUseCase;
        this.findGameByIdOrSlugOrExternalUseCase = findGameByIdOrSlugOrExternalUseCase;
    }

    @GetMapping
    @Operation(summary = "Find all games", description = "Returns a paginated list of games with sorting options")
    public  ResponseEntity<FindGameResponse> findAll(
            @Valid FindAllGamesDTO dto) {
        logger.info("Fetching all games");
        FindGameResponse response = findGameUseCase.execute(dto);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Find game by ID", description = "Returns a game by its ID")
    public ResponseEntity<GameResponse> findById (@PathVariable(name = "id") String id){
        ObjectId gameId = new ObjectId(id);

        return findGameByIdOrSlugOrExternalUseCase
                .execute(gameId, null, null)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Find game by Slug", description = "Returns a game by its Slug")
    public ResponseEntity<GameResponse> findBySlug (@PathVariable(name = "slug") String slug){

        return findGameByIdOrSlugOrExternalUseCase
                .execute(null,slug, null)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/externalId/{externalId}")
    @Operation(summary = "Find game by external ID", description = "Returns a game by its external ID")
    public ResponseEntity<GameResponse> findByExternalId (@PathVariable(name = "externalId") Integer externalId){

        return findGameByIdOrSlugOrExternalUseCase
                .execute(null, null, externalId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
