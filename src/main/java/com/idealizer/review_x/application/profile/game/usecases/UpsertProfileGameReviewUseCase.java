package com.idealizer.review_x.application.profile.game.usecases;

import com.idealizer.review_x.application.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.application.profile.game.mappers.ProfileGameMapper;
import com.idealizer.review_x.application.profile.game.mappers.ProfileReviewMapper;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import com.idealizer.review_x.domain.profile.game.ProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import com.idealizer.review_x.domain.profile.game.review.ReviewGame;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UpsertProfileGameReviewUseCase {

    private static final Logger logger = Logger.getLogger(UpsertProfileGameReviewUseCase.class.getName());
    ;

    private final GameRepository gameRepository;
    private final ProfileGameRepository profileGameRepository;
    private final ProfileReviewRepository profileReviewRepository;
    private final ProfileGameMapper profileGameMapper;
    private final ProfileReviewMapper profileReviewMapper;
    private final MessageUtil messageUtil;

    public UpsertProfileGameReviewUseCase(GameRepository gameRepository, ProfileGameRepository profileGameRepository,
                                          ProfileReviewRepository profileReviewRepository,
                                          ProfileGameMapper profileGameMapper, ProfileReviewMapper profileReviewMapper,
                                          MessageUtil messageUtil) {
        this.gameRepository = gameRepository;
        this.profileGameRepository = profileGameRepository;
        this.profileReviewRepository = profileReviewRepository;
        this.profileGameMapper = profileGameMapper;
        this.profileReviewMapper = profileReviewMapper;
        this.messageUtil = messageUtil;
    }

    public void execute(UpsertProfileGameReviewCommand command) {
        logger.info("Executing UpsertProfileGameReviewUseCase... {}" + command);

        boolean hasReviewInRequest = command.getReviewContent() != null;

        Optional<ProfileGame> profileGameFound = profileGameRepository.findByGameId(command.getGameId());
        Optional<Game> game = gameRepository.findById(command.getGameId());

        if (!game.isPresent()) {
            logger.warning("Game not found for ID: " + command.getGameId());
            throw new RuntimeException("Game not found");
        }


        if (profileGameFound.isPresent()) {
            ProfileGame profileGame = profileGameFound.get();

            profileGame.setSourcePlatform(command.getSourcePlatform());
            profileGame.setPlayedOn(command.getPlayedOn());
            profileGame.setStatus(command.getStatus());
            profileGame.setPlaytimeInMinutes(command.getPlaytimeInMinutes());
            profileGame.setRating(command.getRating());
            profileGame.setPlaying(command.getPlaying());
            profileGame.setFavorite(command.getFavorite());
            profileGame.setOwned(command.getOwned());
            profileGame.setWishlist(command.getWishlist());
            profileGame.setMastered(command.getMastered());
            profileGame.setFavoriteScreenshots(command.getFavoriteScreenshots());
            profileGame.setGameName(game.get().getName());
            profileGame.setGameSlug(game.get().getSlug());
            profileGame.setGameCover(game.get().getCover());

            ProfileGame updatedProfileGame = profileGameRepository.save(profileGame);

            if (hasReviewInRequest) {
                Optional<ReviewGame> existingReview = profileReviewRepository.findByProfileGameId(profileGame.getId());


                if (existingReview.isEmpty()) {
                    ReviewGame review = profileReviewMapper.toEntity(command);
                    review.setUserId(command.getUserId());
                    review.setGameId(command.getGameId());
                    review.setProfileGameId(updatedProfileGame.getId());
                    review.setGameName(game.get().getName());
                    review.setGameSlug(game.get().getSlug());
                    review.setGameCover(game.get().getCover());
                    profileReviewRepository.save(review);

                    updatedProfileGame.setHasReview(true);
                    profileGameRepository.save(updatedProfileGame);

                } else {
                    ReviewGame review = existingReview.get();

                    review.setTitle(command.getReviewTitle());
                    review.setContent(command.getReviewContent());
                    review.setSpoiler(command.getReviewSpoiler());
                    review.setReplay(command.getReviewReplay());
                    review.setGameName(game.get().getName());
                    review.setGameSlug(game.get().getSlug());
                    review.setGameCover(game.get().getCover());

                    profileReviewRepository.save(review);
                    logger.info("Review already exists, updating editable fields...");
                }

            }

            logger.info("Profile game already exists, updating...");
        } else {
            logger.info("Profile game not found, creating...");

            ProfileGame profileGame = profileGameMapper.toEntity(command);
            profileGame.setGameName(game.get().getName());
            profileGame.setGameSlug(game.get().getSlug());
            profileGame.setGameCover(game.get().getCover());
            ProfileGame createdProfileGame = profileGameRepository.save(profileGame);

            logger.info("Profile game created..");


            if (hasReviewInRequest) {
                ReviewGame review = profileReviewMapper.toEntity(command);
                review.setUserId(command.getUserId());
                review.setGameId(command.getGameId());
                review.setProfileGameId(createdProfileGame.getId());
                review.setGameName(game.get().getName());
                review.setGameSlug(game.get().getSlug());
                review.setGameCover(game.get().getCover());
                ReviewGame createdReview = profileReviewRepository.save(review);

                createdProfileGame.setHasReview(true);
                profileGameRepository.save(createdProfileGame);
                logger.info("Review created for profile game: " + createdReview.getId());
            }

        }


    }
}
