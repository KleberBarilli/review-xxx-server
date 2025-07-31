package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.application.games.profile.game.commands.CreateUpdateProfileGameCommand;
import com.idealizer.review_x.application.games.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.application.games.profile.game.mappers.ProfileGameMapper;
import com.idealizer.review_x.application.games.profile.review.commands.CreateUpdateReviewCommand;
import com.idealizer.review_x.application.games.profile.review.mappers.ReviewMapper;
import com.idealizer.review_x.application.games.profile.review.usecases.CreateReviewUseCase;
import com.idealizer.review_x.application.games.profile.review.usecases.UpdateReviewUseCase;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import com.idealizer.review_x.domain.profile.game.ProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import com.idealizer.review_x.domain.profile.game.review.ReviewGame;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UpsertProfileGameReviewUseCase {

    private static final Logger logger = Logger.getLogger(UpsertProfileGameReviewUseCase.class.getName());
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UpsertProfileGameReviewUseCase.class);
    ;

    private final GameRepository gameRepository;
    private final ProfileGameRepository profileGameRepository;
    private final ProfileReviewRepository profileReviewRepository;
    private final ProfileGameMapper profileGameMapper;
    private final ReviewMapper profileReviewMapper;
    private final MessageUtil messageUtil;
    private final CreateProfileGameUseCase createProfileGameUseCase;
    private final UpdateProfileGameUseCase updateProfileGameUseCase;
    private final CreateReviewUseCase createReviewUseCase;
    private final UpdateReviewUseCase updateReviewUseCase;

    public UpsertProfileGameReviewUseCase(GameRepository gameRepository, ProfileGameRepository profileGameRepository,
                                          ProfileReviewRepository profileReviewRepository,
                                          ProfileGameMapper profileGameMapper, ReviewMapper profileReviewMapper,
                                          MessageUtil messageUtil, CreateProfileGameUseCase createProfileGameUseCase,
                                          UpdateProfileGameUseCase updateProfileGameUseCase,
                                          CreateReviewUseCase createReviewUseCase,
                                          UpdateReviewUseCase updateReviewUseCase) {
        this.gameRepository = gameRepository;
        this.profileGameRepository = profileGameRepository;
        this.profileReviewRepository = profileReviewRepository;
        this.profileGameMapper = profileGameMapper;
        this.profileReviewMapper = profileReviewMapper;
        this.messageUtil = messageUtil;
        this.createProfileGameUseCase = createProfileGameUseCase;
        this.updateProfileGameUseCase = updateProfileGameUseCase;
        this.createReviewUseCase = createReviewUseCase;
        this.updateReviewUseCase = updateReviewUseCase;
    }

    public void execute(UpsertProfileGameReviewCommand command, ObjectId userId) {
        boolean hasReviewInRequest =
                command.getReviewTitle() != null ||
                        command.getReviewContent() != null ||
                        command.getReviewStartedAt() != null ||
                        command.getReviewFinishedAt() != null ||
                        command.getReviewSpoiler() != null ||
                        command.getReviewReplay() != null;



        Optional<ProfileGame> profileGameFound = profileGameRepository.findByGameId(command.getGameId());
        Optional<Game> game = gameRepository.findById(command.getGameId());

        if (!game.isPresent()) {
            logger.warning("Game not found for ID: " + command.getGameId());
            throw new RuntimeException("Game not found");
        }


        if (profileGameFound.isPresent()) {
            ProfileGame profileGame = profileGameFound.get();

//            profileGame.setSourcePlatform(command.getSourcePlatform());
//            profileGame.setPlayedOn(command.getPlayedOn());
//            profileGame.setStatus(command.getStatus());
//            profileGame.setPlaytimeInMinutes(command.getPlaytimeInMinutes());
//            profileGame.setRating(command.getRating());
//            profileGame.setPlaying(command.getPlaying());
//            profileGame.setFavorite(command.getFavorite());
//            profileGame.setOwned(command.getOwned());
//            profileGame.setWishlist(command.getWishlist());
//            profileGame.setMastered(command.getMastered());
//            profileGame.setFavoriteScreenshots(command.getFavoriteScreenshots());
//            profileGame.setGameName(game.get().getName());
//            profileGame.setGameSlug(game.get().getSlug());
//            profileGame.setGameCover(game.get().getCover());

            //updating works :)


            profileGameMapper.updateProfileGameFromCommand(command, profileGame);

            updateProfileGameUseCase.execute(profileGame);


            if (hasReviewInRequest) {
                Optional<ReviewGame> existingReview = profileReviewRepository.findByProfileGameId(profileGame.getId());
                if (existingReview.isEmpty()) {

                    CreateUpdateReviewCommand reviewCommand =
                            profileReviewMapper.toCommand(command);

                    reviewCommand.setUserId(userId);
                    reviewCommand.setProfileGameId(profileGame.getId());
                    reviewCommand.setGameName(game.get().getName());
                    reviewCommand.setGameSlug(game.get().getSlug());
                    reviewCommand.setGameCover(game.get().getCover());
                    createReviewUseCase.execute(reviewCommand);


                } else {
                      ReviewGame review = existingReview.get();
//                    review.setTitle(command.getReviewTitle());
//                    review.setContent(command.getReviewContent());
//                    review.setSpoiler(command.getReviewSpoiler());
//                    review.setReplay(command.getReviewReplay());
//                    review.setStartedAt(command.getReviewStartedAt());
//                    review.setFinishedAt(command.getReviewFinishedAt());
//                    review.setGameName(game.get().getName());
//                    review.setGameSlug(game.get().getSlug());
//                    review.setGameCover(game.get().getCover());

                    // not updating...
                    log.info("Updating review title to: {}", command.getReviewTitle());

                    profileReviewMapper.updateReviewFromCommand(command, review);
                    log.info("Review entity now has title: {}", review.getTitle());

                    updateReviewUseCase.execute(review);
                }

            }

        } else {
            CreateUpdateProfileGameCommand profileGameCommand =
                    profileGameMapper.toCreateUpdateCommand(command);
            profileGameCommand.setUserId(userId);
            profileGameCommand.setGameName(game.get().getName());
            profileGameCommand.setGameSlug(game.get().getSlug());
            profileGameCommand.setGameCover(game.get().getCover());
            if (hasReviewInRequest) profileGameCommand.setHasReview(true);


            ObjectId profileGameId =  createProfileGameUseCase.execute(profileGameCommand);
            CreateUpdateReviewCommand reviewCommand =
                    profileReviewMapper.toCommand(command);

            if(hasReviewInRequest){
                reviewCommand.setGameName(game.get().getName());
                reviewCommand.setGameSlug(game.get().getSlug());
                reviewCommand.setGameCover(game.get().getCover());
                reviewCommand.setUserId(userId);
                reviewCommand.setProfileGameId(profileGameId);
                createReviewUseCase.execute(reviewCommand);
            }

        }

    }
}
