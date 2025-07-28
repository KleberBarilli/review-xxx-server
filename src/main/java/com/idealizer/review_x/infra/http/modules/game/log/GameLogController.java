package com.idealizer.review_x.infra.http.modules.game.log;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/game-logs")
public class GameLogController {

    private static Logger logger = Logger.getLogger(GameLogController.class.getName());

    @PostMapping
    public void upsertGameLog(@RequestBody String review, @AuthenticationPrincipal UserDetails user) {

        logger.log(Level.INFO, "Upserting Game Log"
                + "\nUser: " + user.getUsername()
                + "\nReview: " + review);

    }

    @GetMapping
    public String getGameLogs(@AuthenticationPrincipal UserDetails user) {
        logger.log(Level.INFO, "Fetching Game Logs for user: " + user.getUsername());
        return "Game logs fetched successfully for user: " + user.getUsername();
    }
}
