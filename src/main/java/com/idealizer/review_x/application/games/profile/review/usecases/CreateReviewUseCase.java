package com.idealizer.review_x.application.games.profile.review.usecases;

import com.idealizer.review_x.application.games.profile.review.commands.CreateUpdateReviewCommand;
import com.idealizer.review_x.application.games.profile.review.mappers.ReviewMapper;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CreateReviewUseCase {
    private final Logger logger = Logger.getLogger(CreateReviewUseCase.class.getName());

    private final ProfileReviewRepository profileReviewRepository;
    private final ReviewMapper reviewMapper;

    public CreateReviewUseCase(ProfileReviewRepository profileReviewRepository, ReviewMapper profileReviewMapper) {
        this.profileReviewRepository = profileReviewRepository;
        this.reviewMapper = profileReviewMapper;
    }

    public void execute(CreateUpdateReviewCommand command) {
        profileReviewRepository.save(reviewMapper.toEntity(command));
        logger.info("Review created successfully for profile-game ID: " + command.getProfileGameId());

    }
}

