package com.idealizer.review_x.application.profile.review;

import com.idealizer.review_x.application.review.usecases.UpdateReviewUseCase;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import com.idealizer.review_x.domain.core.review.entities.Review;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
public class UpdateReviewUseCaseTest {

    private ReviewRepository reviewRepository;
    private UpdateReviewUseCase updateReviewUseCase;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        updateReviewUseCase = new UpdateReviewUseCase(reviewRepository);
    }

    @Test
    void shouldUpdateReview() {
        Review review = new Review();
        review.setContent("Updated content");
        review.setProfileTargetId(new ObjectId());

        updateReviewUseCase.execute(review);

        verify(reviewRepository, times(1)).save(review);
    }
}
