package com.idealizer.review_x.infra.http.modules.game.profile;

import com.idealizer.review_x.application.games.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.application.games.profile.game.usecases.UpsertProfileGameReviewUseCase;
import com.idealizer.review_x.domain.profile.game.ProfileGameStatus;
import com.idealizer.review_x.infra.http.modules.game.profile.dto.UpsertProfileGameDTO;
import com.idealizer.review_x.infra.http.modules.game.profile.mappers.ProfileGameDTOMapper;
import com.idealizer.review_x.security.services.UserDetailsImpl;
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

        ObjectId userId = ((UserDetailsImpl) user).getId();

        UpsertProfileGameReviewCommand command = profileGameMapper.toCommand(dto);
        command.setGameId(new ObjectId(gameId));
        upsertProfileGameReviewUseCase.execute(command, userId);



    }

    @GetMapping
    public String getGameLogs(@AuthenticationPrincipal UserDetails user) {
        logger.log(Level.INFO, "Fetching Game Logs for user: " + user.getUsername());
        return "Game logs fetched successfully for user: " + user.getUsername();
    }
}
