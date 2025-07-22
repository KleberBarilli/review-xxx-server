package com.idealizer.review_x.application.modules.games.controllers;

import com.idealizer.review_x.application.modules.games.controllers.dto.FindGamesResponseDTO;
import com.idealizer.review_x.application.modules.games.controllers.dto.GameResponseDTO;
import com.idealizer.review_x.application.modules.games.controllers.dto.SimpleGameResponseDTO;
import com.idealizer.review_x.application.modules.games.controllers.mappers.GameMapper;
import com.idealizer.review_x.application.modules.games.services.FindGameByIdService;
import com.idealizer.review_x.application.modules.games.services.FindManyGamesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final GameMapper gameMapper;

    public GameController(FindManyGamesService findManyGamesService, FindGameByIdService findGameByIdService , GameMapper gameMapper) {
        this.findManyGamesService = findManyGamesService;
        this.findGameByIdService = findGameByIdService;
        this.gameMapper = gameMapper;
    }

    @GetMapping
    @Operation(summary = "Find all games", description = "Returns a paginated list of games with sorting options")
    public FindGamesResponseDTO findAll(
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", defaultValue = "total_rating_count") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order) {
        logger.info("Fetching all games");
        return findManyGamesService.execute(limit, pageNumber, sort, order);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find game by ID", description = "Returns a game by its ID")
    public ResponseEntity<GameResponseDTO> findById (@PathVariable(name = "id") String id){
        ObjectId gameId = new ObjectId(id);

        return findGameByIdService
                .execute(gameId)
                .map(game -> {
                    GameResponseDTO dto = gameMapper.toDetailedDomain(game);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
