package com.idealizer.review_x.application.modules.games.controllers;

import com.idealizer.review_x.application.AppConstants;
import com.idealizer.review_x.application.modules.games.controllers.dto.FindGamesResponse;
import com.idealizer.review_x.application.modules.games.entities.Game;
import com.idealizer.review_x.application.modules.games.services.FindManyGamesService;
import com.idealizer.review_x.application.modules.games.services.implementations.FindManyGamesServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Games")
public class GameController {

    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    private final FindManyGamesService findManyGamesService;

    public GameController(FindManyGamesService findManyGamesService) {
        this.findManyGamesService = findManyGamesService;
    }

    @GetMapping
    @Operation(summary = "Find all games", description = "Returns a paginated list of games with sorting options")
    public FindGamesResponse findAll(
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", defaultValue = "total_rating_count") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order) {
        logger.info("Fetching all games");
        return findManyGamesService.execute(limit, pageNumber, sort, order);
    }

}
