package com.idealizer.review_x.modules.games.controllers;

import com.idealizer.review_x.modules.games.entities.Game;
import com.idealizer.review_x.modules.games.services.FindManyGamesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<Game> findAll() {
        logger.info("Fetching all games");
        return findManyGamesService.execute();
    }

}
