package com.idealizer.review_x.infra.http.modules.game;

import com.idealizer.review_x.application.modules.games.services.outputs.FindGamesResponseDTO;
import com.idealizer.review_x.application.modules.games.services.outputs.GameResponseDTO;
import com.idealizer.review_x.application.modules.games.services.FindGameByIdService;
import com.idealizer.review_x.application.modules.games.services.FindManyGamesService;
import com.idealizer.review_x.infra.http.modules.game.dto.FindAllGamesDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Games")
public class GameController {

    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    private final FindManyGamesService findManyGamesService;
    private final FindGameByIdService findGameByIdService;

    public GameController(FindManyGamesService findManyGamesService, FindGameByIdService findGameByIdService) {
        this.findManyGamesService = findManyGamesService;
        this.findGameByIdService = findGameByIdService;
    }

    @GetMapping
    @Operation(summary = "Find all games", description = "Returns a paginated list of games with sorting options")
    public  ResponseEntity<FindGamesResponseDTO> findAll(
            @Valid FindAllGamesDTO dto) {
        logger.info("Fetching all games");
        FindGamesResponseDTO response = findManyGamesService.execute( dto.limit(), dto.pageNumber(), dto.sort(), dto.order());
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Find game by ID", description = "Returns a game by its ID")
    public ResponseEntity<GameResponseDTO> findById (@PathVariable(name = "id") String id){
        ObjectId gameId = new ObjectId(id);

        return findGameByIdService
                .execute(gameId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
