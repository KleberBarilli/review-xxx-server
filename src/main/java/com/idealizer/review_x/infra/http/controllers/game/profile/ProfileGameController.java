package com.idealizer.review_x.infra.http.controllers.game.profile;

import com.idealizer.review_x.application.games.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.application.games.profile.game.log.usecases.CreateProfileGameLogUseCase;
import com.idealizer.review_x.application.games.profile.game.log.usecases.RemoveProfileGameLogUseCase;
import com.idealizer.review_x.application.games.profile.game.log.usecases.UpdateProfileGameLogUseCase;
import com.idealizer.review_x.application.games.profile.game.responses.PublicProfileGameResponse;
import com.idealizer.review_x.application.games.profile.game.usecases.FindProfileGameBySlugAndUsernameUseCase;
import com.idealizer.review_x.application.games.profile.game.usecases.UpdateFavoriteGamesUseCase;
import com.idealizer.review_x.application.games.profile.game.usecases.UpsertProfileGameReviewUseCase;
import com.idealizer.review_x.application.games.profile.review.usecases.DeleteReviewUseCase;
import com.idealizer.review_x.common.dtos.profile.game.UpdateFavoriteGameDTO;
import com.idealizer.review_x.common.dtos.profile.game.UpsertProfileGameLogDTO;
import com.idealizer.review_x.common.exceptions.ForbiddenException;
import com.idealizer.review_x.infra.http.controllers.game.profile.dto.UpsertProfileGameDTO;
import com.idealizer.review_x.infra.http.controllers.game.profile.mappers.ProfileGameDTOMapper;
import com.idealizer.review_x.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/profile-games")
@Tag(name = "Profile Games")
public class ProfileGameController {

    private static Logger logger = Logger.getLogger(ProfileGameController.class.getName());

    private final ProfileGameDTOMapper profileGameMapper;
    private final UpsertProfileGameReviewUseCase upsertProfileGameReviewUseCase;
    private final DeleteReviewUseCase deleteReviewUseCase;
    private final UpdateFavoriteGamesUseCase updateFavoriteGamesUseCase;
    private final FindProfileGameBySlugAndUsernameUseCase findProfileGameBySlugAndUsernameUseCase;
    private final CreateProfileGameLogUseCase createProfileGameLogUseCase;
    private final UpdateProfileGameLogUseCase updateProfileGameLogUseCase;
    private final RemoveProfileGameLogUseCase removeProfileGameLogUseCase;

    public ProfileGameController(ProfileGameDTOMapper profileGameMapper,
                                 UpsertProfileGameReviewUseCase upsertProfileGameReviewUseCase,
                                 DeleteReviewUseCase deleteReviewUseCase,
                                 UpdateFavoriteGamesUseCase updateFavoriteGamesUseCase,
                                 FindProfileGameBySlugAndUsernameUseCase findProfileGameBySlugAndUsernameUseCase,
                                 CreateProfileGameLogUseCase createProfileGameLogUseCase,
                                 UpdateProfileGameLogUseCase updateProfileGameLogUseCase,
                                    RemoveProfileGameLogUseCase removeProfileGameLogUseCase
    ) {
        this.profileGameMapper = profileGameMapper;
        this.upsertProfileGameReviewUseCase = upsertProfileGameReviewUseCase;
        this.deleteReviewUseCase = deleteReviewUseCase;
        this.updateFavoriteGamesUseCase = updateFavoriteGamesUseCase;
        this.findProfileGameBySlugAndUsernameUseCase = findProfileGameBySlugAndUsernameUseCase;
        this.createProfileGameLogUseCase = createProfileGameLogUseCase;
        this.updateProfileGameLogUseCase = updateProfileGameLogUseCase;
        this.removeProfileGameLogUseCase = removeProfileGameLogUseCase;
    }

    @Operation(summary = "Create/Update Profile Game + Review")
    @PutMapping("/{gameId}")
    public void upsertProfileGame(@AuthenticationPrincipal UserDetails user,
                                  @PathVariable() String gameId,
                                  @Valid @RequestBody UpsertProfileGameDTO dto) {

        ObjectId userId = ((UserDetailsImpl) user).getId();

        UpsertProfileGameReviewCommand command = profileGameMapper.toCommand(dto);
        command.setGameId(new ObjectId(gameId));
        upsertProfileGameReviewUseCase.execute(command, userId, user.getUsername());


    }

    @GetMapping
    public String getGameLogs(@AuthenticationPrincipal UserDetails user) {
        logger.log(Level.INFO, "Fetching Game Logs for user: " + user.getUsername());
        return "Game logs fetched successfully for user: " + user.getUsername();
    }

    @GetMapping("/public/{username}/{gameSlug}")
    public ResponseEntity<Optional<PublicProfileGameResponse>> getPublicGameInfoFromUser(
            @PathVariable String username, @PathVariable String gameSlug,
            @RequestParam(required = false, defaultValue = "false") Boolean includeComments,
            @RequestParam(required = false, defaultValue = "false") Boolean includeLogs) {

        Optional<PublicProfileGameResponse> response = findProfileGameBySlugAndUsernameUseCase.execute(gameSlug,
                username, includeComments, includeLogs);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Set favorite games (max10)", description = "All favorites must be sent")
    @PutMapping("/set/favorites")
    public void setFavorites(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody UpdateFavoriteGameDTO dto) {
        ObjectId userId = ((UserDetailsImpl) user).getId();
        updateFavoriteGamesUseCase.execute(userId, dto);

    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@AuthenticationPrincipal UserDetails user, @PathVariable String id) {
        ObjectId userId = ((UserDetailsImpl) user).getId();
        ObjectId reviewId = new ObjectId(id);

        try {
            deleteReviewUseCase.execute(userId, reviewId);
            return ResponseEntity.ok().build();
        } catch (ForbiddenException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/logs")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLog(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody UpsertProfileGameLogDTO dto) {
        ObjectId userId = ((UserDetailsImpl) user).getId();
        createProfileGameLogUseCase.execute(dto, userId);
    }

    @PutMapping("/logs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLog(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody UpsertProfileGameLogDTO dto,
            @PathVariable(name = "id") String id
    ) {
        ObjectId userId = ((UserDetailsImpl) user).getId();
        updateProfileGameLogUseCase.execute(new ObjectId(id), dto, userId);
    }
    @DeleteMapping("/logs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLog(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable(name = "id") String id
    ) {
        ObjectId userId = ((UserDetailsImpl) user).getId();
        removeProfileGameLogUseCase.execute(new ObjectId(id), userId);
    }
}
