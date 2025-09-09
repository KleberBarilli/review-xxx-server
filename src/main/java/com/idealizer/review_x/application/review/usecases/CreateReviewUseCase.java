package com.idealizer.review_x.application.review.usecases;

import com.idealizer.review_x.application.review.commands.CreateUpdateReviewCommand;
import com.idealizer.review_x.application.review.mappers.ReviewMapper;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CreateReviewUseCase {
    private final Logger logger = Logger.getLogger(CreateReviewUseCase.class.getName());

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public CreateReviewUseCase(ReviewRepository reviewRepository, ReviewMapper profileReviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = profileReviewMapper;
    }

    public void execute(CreateUpdateReviewCommand command) {
        reviewRepository.save(reviewMapper.toEntity(command));
        logger.info("Review created successfully for profile-target ID: " + command.getProfileTargetId());

    }
}

