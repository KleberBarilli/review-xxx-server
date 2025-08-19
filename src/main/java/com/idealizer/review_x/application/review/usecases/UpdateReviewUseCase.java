package com.idealizer.review_x.application.review.usecases;

import com.idealizer.review_x.domain.review.entities.Review;
import com.idealizer.review_x.domain.review.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UpdateReviewUseCase {
    private final Logger logger = Logger.getLogger(UpdateReviewUseCase.class.getName());

    private final ReviewRepository reviewRepository;

    public UpdateReviewUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void execute(Review updatedReview) {
        logger.info(updatedReview.getContent());
        reviewRepository.save(updatedReview);
        logger.info("Review updated successfully for profile-game ID: " + updatedReview.getProfileTargetId());

    }
}
