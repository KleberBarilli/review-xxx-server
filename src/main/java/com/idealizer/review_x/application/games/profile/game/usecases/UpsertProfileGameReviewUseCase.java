package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.application.games.profile.game.commands.CreateUpdateProfileGameCommand;
import com.idealizer.review_x.application.games.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.application.games.profile.game.mappers.ProfileGameMapper;
import com.idealizer.review_x.application.review.commands.CreateUpdateReviewCommand;
import com.idealizer.review_x.application.review.mappers.ReviewMapper;
import com.idealizer.review_x.application.review.usecases.CreateReviewUseCase;
import com.idealizer.review_x.application.review.usecases.UpdateReviewUseCase;
import com.idealizer.review_x.domain.LogID;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import com.idealizer.review_x.domain.profile.game.entities.ProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.review.repositories.ReviewRepository;
import com.idealizer.review_x.domain.review.entities.Review;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UpsertProfileGameReviewUseCase {

    private static final Logger logger = Logger.getLogger(UpsertProfileGameReviewUseCase.class.getName());

    private final GameRepository gameRepository;
    private final ProfileGameRepository profileGameRepository;
    private final ReviewRepository reviewRepository;
    private final ProfileGameMapper profileGameMapper;
    private final ReviewMapper profileReviewMapper;
    private final CreateProfileGameUseCase createProfileGameUseCase;
    private final UpdateProfileGameUseCase updateProfileGameUseCase;
    private final CreateReviewUseCase createReviewUseCase;
    private final UpdateReviewUseCase updateReviewUseCase;

    public UpsertProfileGameReviewUseCase(GameRepository gameRepository, ProfileGameRepository profileGameRepository,
                                          ReviewRepository reviewRepository,
                                          ProfileGameMapper profileGameMapper, ReviewMapper profileReviewMapper,
                                          CreateProfileGameUseCase createProfileGameUseCase,
                                          UpdateProfileGameUseCase updateProfileGameUseCase,
                                          CreateReviewUseCase createReviewUseCase,
                                          UpdateReviewUseCase updateReviewUseCase) {
        this.gameRepository = gameRepository;
        this.profileGameRepository = profileGameRepository;
        this.reviewRepository = reviewRepository;
        this.profileGameMapper = profileGameMapper;
        this.profileReviewMapper = profileReviewMapper;
        this.createProfileGameUseCase = createProfileGameUseCase;
        this.updateProfileGameUseCase = updateProfileGameUseCase;
        this.createReviewUseCase = createReviewUseCase;
        this.updateReviewUseCase = updateReviewUseCase;
    }

    public void execute(UpsertProfileGameReviewCommand command, ObjectId userId, String username) {
        boolean hasReviewInRequest =
                command.getReviewTitle() != null ||
                        command.getReviewContent() != null ||
                        command.getReviewStartedAt() != null ||
                        command.getReviewFinishedAt() != null ||
                        command.getReviewSpoiler() != null ||
                        command.getReviewReplay() != null;


        Optional<ProfileGame> profileGameFound = profileGameRepository.findByGameId(command.getTargetId());
        Optional<Game> game = gameRepository.findById(command.getTargetId());

        if (!game.isPresent()) {
            logger.warning("Game not found for ID: " + command.getTargetId());
            throw new RuntimeException("Game not found");
        }

        if (profileGameFound.isPresent()) {
            ProfileGame profileGame = profileGameFound.get();
            profileGameMapper.updateProfileGameFromCommand(command, profileGame);
            updateProfileGameUseCase.execute(profileGame);


            if (hasReviewInRequest) {
                Optional<Review> existingReview = reviewRepository.findByProfileTargetIdAndTargetType(profileGame.getId(), LogID.GAMES);
                if (existingReview.isEmpty()) {

                    CreateUpdateReviewCommand reviewCommand =
                            profileReviewMapper.toCommand(command);

                    reviewCommand.setUserId(userId);
                    reviewCommand.setProfileTargetId(profileGame.getId());
                    reviewCommand.setTargetName(game.get().getName());
                    reviewCommand.setTargetSlug(game.get().getSlug());
                    reviewCommand.setTargetCover(game.get().getCover());
                    createReviewUseCase.execute(reviewCommand);

                } else {
                    Review review = existingReview.get();
                    profileReviewMapper.updateReviewFromCommand(command, review);
                    updateReviewUseCase.execute(review);
                }

            }

        } else {
            CreateUpdateProfileGameCommand profileGameCommand =
                    profileGameMapper.toCreateUpdateCommand(command);
            profileGameCommand.setUserId(userId);
            profileGameCommand.setUsername(username);
            profileGameCommand.setGameName(game.get().getName());
            profileGameCommand.setGameSlug(game.get().getSlug());
            profileGameCommand.setGameCover(game.get().getCover());
            if (hasReviewInRequest) profileGameCommand.setHasReview(true);

            ObjectId profileGameId = createProfileGameUseCase.execute(profileGameCommand);
            CreateUpdateReviewCommand reviewCommand =
                    profileReviewMapper.toCommand(command);

            if (hasReviewInRequest) {
                reviewCommand.setTargetName(game.get().getName());
                reviewCommand.setTargetSlug(game.get().getSlug());
                reviewCommand.setTargetCover(game.get().getCover());
                reviewCommand.setUserId(userId);
                reviewCommand.setProfileTargetId(profileGameId);
                createReviewUseCase.execute(reviewCommand);
            }
        }
    }
}
