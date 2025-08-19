package com.idealizer.review_x.infra.http.controllers.game.profile;

import com.idealizer.review_x.application.games.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.application.games.profile.game.log.usecases.CreateProfileGameLogUseCase;
import com.idealizer.review_x.application.games.profile.game.log.usecases.RemoveProfileGameLogUseCase;
import com.idealizer.review_x.application.games.profile.game.log.usecases.UpdateProfileGameLogUseCase;
import com.idealizer.review_x.application.games.profile.game.responses.PublicProfileGameDetailedResponse;
import com.idealizer.review_x.application.games.profile.game.usecases.FindProfileGameBySlugAndUsernameUseCase;
import com.idealizer.review_x.application.games.profile.game.usecases.FindProfileGameByUsernameUseCase;
import com.idealizer.review_x.application.games.profile.game.usecases.UpdateFavoriteGamesUseCase;
import com.idealizer.review_x.application.games.profile.game.usecases.UpsertProfileGameReviewUseCase;
import com.idealizer.review_x.application.review.usecases.DeleteReviewUseCase;
import com.idealizer.review_x.common.dtos.PageResponse;
import com.idealizer.review_x.common.dtos.profile.game.FindProfileGamesDTO;
import com.idealizer.review_x.common.dtos.profile.game.UpdateFavoriteGameDTO;
import com.idealizer.review_x.common.dtos.profile.game.UpsertProfileGameLogDTO;
import com.idealizer.review_x.common.exceptions.ForbiddenException;
import com.idealizer.review_x.domain.LogID;
import com.idealizer.review_x.domain.profile.game.entities.ProfileGameStatus;
import com.idealizer.review_x.domain.profile.game.interfaces.BaseProfileGame;
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

import java.util.*;
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
    private final FindProfileGameByUsernameUseCase findProfileGameByUsernameUseCase;
    private final CreateProfileGameLogUseCase createProfileGameLogUseCase;
    private final UpdateProfileGameLogUseCase updateProfileGameLogUseCase;
    private final RemoveProfileGameLogUseCase removeProfileGameLogUseCase;

    public ProfileGameController(ProfileGameDTOMapper profileGameMapper,
                                 UpsertProfileGameReviewUseCase upsertProfileGameReviewUseCase,
                                 DeleteReviewUseCase deleteReviewUseCase,
                                 UpdateFavoriteGamesUseCase updateFavoriteGamesUseCase,
                                 FindProfileGameBySlugAndUsernameUseCase findProfileGameBySlugAndUsernameUseCase,
                                 FindProfileGameByUsernameUseCase findProfileGameByUsernameUseCase,
                                 CreateProfileGameLogUseCase createProfileGameLogUseCase,
                                 UpdateProfileGameLogUseCase updateProfileGameLogUseCase,
                                 RemoveProfileGameLogUseCase removeProfileGameLogUseCase
    ) {
        this.profileGameMapper = profileGameMapper;
        this.upsertProfileGameReviewUseCase = upsertProfileGameReviewUseCase;
        this.deleteReviewUseCase = deleteReviewUseCase;
        this.updateFavoriteGamesUseCase = updateFavoriteGamesUseCase;
        this.findProfileGameBySlugAndUsernameUseCase = findProfileGameBySlugAndUsernameUseCase;
        this.findProfileGameByUsernameUseCase = findProfileGameByUsernameUseCase;
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
        command.setTargetId(new ObjectId(gameId));
        command.setReviewType(LogID.GAMES);
        upsertProfileGameReviewUseCase.execute(command, userId, user.getUsername());


    }

    @GetMapping("public/{username}")
    public PageResponse<BaseProfileGame> getUserGames(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false)
            Set<ProfileGameStatus> status,
            @RequestParam(required = false) Boolean wishlist,
            @RequestParam(required = false) Boolean owned,
            @RequestParam(required = false) Boolean mastered,
            @RequestParam(required = false) Boolean liked,
            @RequestParam(required = false) Integer ratingMin,
            @RequestParam(required = false) Integer ratingMax
    ) {

        FindProfileGamesDTO f = new FindProfileGamesDTO(
                pageNumber, limit, sort, order,
                status, wishlist, owned, mastered, liked, ratingMin, ratingMax
        );
        return findProfileGameByUsernameUseCase.execute(username, f);

    }

    @GetMapping("/public/{username}/{gameSlug}")
    public ResponseEntity<Optional<PublicProfileGameDetailedResponse>> getPublicGameInfoFromUser(
            @PathVariable String username, @PathVariable String gameSlug,
            @RequestParam(required = false, defaultValue = "false") Boolean includeComments,
            @RequestParam(required = false, defaultValue = "false") Boolean includeLogs) {

        Optional<PublicProfileGameDetailedResponse> response = findProfileGameBySlugAndUsernameUseCase.execute(gameSlug,
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
