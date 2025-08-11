package com.idealizer.review_x.infra.http.modules.game;

import com.idealizer.review_x.application.games.game.responses.FindGameResponse;
import com.idealizer.review_x.application.games.game.responses.GameResponse;
import com.idealizer.review_x.application.games.game.usecases.FindGameByIdUseCase;
import com.idealizer.review_x.application.games.game.usecases.FindGameUseCase;
import com.idealizer.review_x.infra.http.modules.game.dto.FindAllGamesDTO;
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
    private final FindGameByIdUseCase findGameByIdUseCase;

    public GameController(FindGameUseCase findGameUseCase, FindGameByIdUseCase findGameByIdUseCase) {
        this.findGameUseCase = findGameUseCase;
        this.findGameByIdUseCase = findGameByIdUseCase;
    }

    @GetMapping
    @Operation(summary = "Find all games", description = "Returns a paginated list of games with sorting options")
    public  ResponseEntity<FindGameResponse> findAll(
            @Valid FindAllGamesDTO dto) {
        logger.info("Fetching all games");
        FindGameResponse response = findGameUseCase.execute( dto.limit(), dto.pageNumber(), dto.sort(), dto.order(), dto.slug());
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Find game by ID", description = "Returns a game by its ID")
    public ResponseEntity<GameResponse> findById (@PathVariable(name = "id") String id){
        ObjectId gameId = new ObjectId(id);

        return findGameByIdUseCase
                .execute(gameId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
