package com.idealizer.review_x.application.profile.review;

import com.idealizer.review_x.application.games.profile.review.usecases.UpdateReviewUseCase;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import com.idealizer.review_x.domain.profile.game.entities.ReviewGame;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
public class UpdateReviewUseCaseTest {

    private ProfileReviewRepository profileReviewRepository;
    private UpdateReviewUseCase updateReviewUseCase;

    @BeforeEach
    void setUp() {
        profileReviewRepository = mock(ProfileReviewRepository.class);
        updateReviewUseCase = new UpdateReviewUseCase(profileReviewRepository);
    }

    @Test
    void shouldUpdateReview() {
        ReviewGame review = new ReviewGame();
        review.setContent("Updated content");
        review.setProfileGameId(new ObjectId());

        updateReviewUseCase.execute(review);

        verify(profileReviewRepository, times(1)).save(review);
    }
}
