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
        profileReviewRepository.save(updatedReview);
        logger.info("Review updated successfully for profile-game ID: " + updatedReview.getProfileGameId());

    }
}
