package com.idealizer.review_x.application.games.profile.review.usecases;

import com.idealizer.review_x.application.games.profile.review.commands.CreateUpdateReviewCommand;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import com.idealizer.review_x.domain.profile.game.review.ReviewGame;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UpdateReviewUseCase {
    private final Logger logger = Logger.getLogger(UpdateReviewUseCase.class.getName());

    private final ProfileReviewRepository profileReviewRepository;

    public UpdateReviewUseCase(ProfileReviewRepository profileReviewRepository) {
        this.profileReviewRepository = profileReviewRepository;
    }

    public void execute(ReviewGame updatedReview) {
        logger.info(updatedReview.getContent());
        logger.info("Updating review for profile-game ID: " + updatedReview.getProfileGameId());
        logger.info(updatedReview.getGameCover());
        logger.info("Review title: " + updatedReview.getTitle());
        logger.info("Review content: " + updatedReview.getContent());
        logger.info("Review spoiler: " + updatedReview.getSpoiler());
        logger.info("Review replay: " + updatedReview.getReplay());
        logger.info("Review started at: " + updatedReview.getStartedAt());
        logger.info("Review finished at: " + updatedReview.getFinishedAt());

        profileReviewRepository.save(updatedReview);
        logger.info("Review updated successfully for profile-game ID: " + updatedReview.getProfileGameId());

    }
}
