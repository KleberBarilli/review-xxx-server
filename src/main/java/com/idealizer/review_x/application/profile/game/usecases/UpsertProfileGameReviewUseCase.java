package com.idealizer.review_x.application.profile.game.usecases;

import com.idealizer.review_x.application.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.application.profile.game.mappers.ProfileGameMapper;
import com.idealizer.review_x.application.profile.game.mappers.ProfileReviewMapper;
import com.idealizer.review_x.common.MessageUtil;
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

    private ProfileGameRepository profileGameRepository;
    private ProfileReviewRepository profileReviewRepository;
    private ProfileGameMapper profileGameMapper;
    private ProfileReviewMapper profileReviewMapper;
    private final MessageUtil messageUtil;

    public UpsertProfileGameReviewUseCase(ProfileGameRepository profileGameRepository,
                                          ProfileReviewRepository profileReviewRepository,
                                          ProfileGameMapper profileGameMapper, ProfileReviewMapper profileReviewMapper,
                                          MessageUtil messageUtil) {
        this.profileGameRepository = profileGameRepository;
        this.profileReviewRepository = profileReviewRepository;
        this.profileGameMapper = profileGameMapper;
        this.profileReviewMapper = profileReviewMapper;
        this.messageUtil = messageUtil;
    }

    public void execute(UpsertProfileGameReviewCommand command) {
        logger.info("Executing UpsertProfileGameReviewUseCase... {}" + command);

        boolean hasReviewInRequest = command.getReviewContent() != null;

        Optional<ProfileGame> profileGame = profileGameRepository.findByGameId(command.getGameId());

        if (profileGame.isPresent()) {
            ProfileGame updatedProfileGame = profileGameRepository.save(profileGameMapper.toEntity(command));

            if (hasReviewInRequest) {
                ReviewGame review = profileReviewMapper.toEntity(command);

                if (!profileReviewRepository.existsByProfileGameId(updatedProfileGame.getId())) {

                    updatedProfileGame.setHasReview(true);
                    profileGameRepository.save(updatedProfileGame);

                    logger.info("Review not found for profile game, creating new review and set has review...");
                }

                review.setUserId(command.getUserId());
                review.setGameId(command.getGameId());
                review.setProfileGameId(updatedProfileGame.getId());
                profileReviewRepository.save(review);
            }


            logger.info("Profile game already exists, updating...");
        } else {
            logger.info("Profile game not found, creating...");

            ProfileGame createdProfileGame = profileGameRepository.save(profileGameMapper.toEntity(command));

            logger.info("Profile game created..");


            if (hasReviewInRequest) {
                ReviewGame review = profileReviewMapper.toEntity(command);
                review.setUserId(command.getUserId());
                review.setGameId(command.getGameId());
                review.setProfileGameId(createdProfileGame.getId());
                ReviewGame createdReview = profileReviewRepository.save(review);

                createdProfileGame.setHasReview(true);
                profileGameRepository.save(createdProfileGame);
                logger.info("Review created for profile game: " + createdReview.getId());
            }

        }


    }
}
