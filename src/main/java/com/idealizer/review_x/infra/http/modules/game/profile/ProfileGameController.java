package com.idealizer.review_x.infra.http.modules.game.profile;

import com.idealizer.review_x.application.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.application.profile.game.usecases.UpsertProfileGameReviewUseCase;
import com.idealizer.review_x.infra.http.modules.game.profile.dto.UpsertProfileGameDTO;
import com.idealizer.review_x.infra.http.modules.game.profile.mappers.ProfileGameDTOMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/profile-games")
@Tag(name = "Profile Games")
public class ProfileGameController {

    private static Logger logger = Logger.getLogger(ProfileGameController.class.getName());

    private final ProfileGameDTOMapper profileGameMapper;
    private final UpsertProfileGameReviewUseCase upsertProfileGameReviewUseCase;

    public ProfileGameController(ProfileGameDTOMapper profileGameMapper,
                                 UpsertProfileGameReviewUseCase upsertProfileGameReviewUseCase) {
        this.profileGameMapper = profileGameMapper;
        this.upsertProfileGameReviewUseCase = upsertProfileGameReviewUseCase;
    }

    @PutMapping("/{gameId}")
    public void upsertProfileGame(@AuthenticationPrincipal UserDetails user,
                                  @RequestParam(defaultValue = "687f03e5c4edbe29b5eef3bb") String gameId,
                                  @Valid @RequestBody UpsertProfileGameDTO dto) {

        UpsertProfileGameReviewCommand command = profileGameMapper.toCommand(dto);
        command.setGameId(new ObjectId(gameId));
        upsertProfileGameReviewUseCase.execute(command);

        logger.log(Level.INFO, "Upserting Game Log"
                + "\nUser: " + user.getUsername()
                + "\nGame ID: " + gameId
                + "\nReview: " + dto);


    }

    @GetMapping
    public String getGameLogs(@AuthenticationPrincipal UserDetails user) {
        logger.log(Level.INFO, "Fetching Game Logs for user: " + user.getUsername());
        return "Game logs fetched successfully for user: " + user.getUsername();
    }
}
