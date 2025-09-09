package com.idealizer.review_x.application.profile.review;

import com.idealizer.review_x.application.review.commands.CreateUpdateReviewCommand;
import com.idealizer.review_x.application.review.mappers.ReviewMapper;
import com.idealizer.review_x.application.review.usecases.CreateReviewUseCase;
import com.idealizer.review_x.domain.core.review.entities.Review;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
public class CreateReviewUseCaseTest {
    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;
    private CreateReviewUseCase createReviewUseCase;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        reviewMapper = mock(ReviewMapper.class);
        createReviewUseCase = new CreateReviewUseCase(reviewRepository, reviewMapper);
    }

    @Test
    void shouldCreateReview() {
        CreateUpdateReviewCommand command = new CreateUpdateReviewCommand();
        command.setProfileTargetId(new ObjectId());
        Review reviewGame = new Review();
        when(reviewMapper.toEntity(command)).thenReturn(reviewGame);
        createReviewUseCase.execute(command);

        verify(reviewMapper, times(1)).toEntity(command);
        verify(reviewRepository, times(1)).save(reviewGame);
    }
}
