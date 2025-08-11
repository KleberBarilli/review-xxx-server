package com.idealizer.review_x.application.profile.review;

import com.idealizer.review_x.application.games.profile.review.commands.CreateUpdateReviewCommand;
import com.idealizer.review_x.application.games.profile.review.mappers.ReviewMapper;
import com.idealizer.review_x.application.games.profile.review.usecases.CreateReviewUseCase;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import com.idealizer.review_x.domain.profile.game.entities.ReviewGame;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
public class CreateReviewUseCaseTest {
    private ProfileReviewRepository profileReviewRepository;
    private ReviewMapper reviewMapper;
    private CreateReviewUseCase createReviewUseCase;

    @BeforeEach
    void setUp() {
        profileReviewRepository = mock(ProfileReviewRepository.class);
        reviewMapper = mock(ReviewMapper.class);
        createReviewUseCase = new CreateReviewUseCase(profileReviewRepository, reviewMapper);
    }

    @Test
    void shouldCreateReview() {
        CreateUpdateReviewCommand command = new CreateUpdateReviewCommand();
        command.setProfileGameId(new ObjectId());
        ReviewGame reviewGame = new ReviewGame();
        when(reviewMapper.toEntity(command)).thenReturn(reviewGame);
        createReviewUseCase.execute(command);

        verify(reviewMapper, times(1)).toEntity(command);
        verify(profileReviewRepository, times(1)).save(reviewGame);
    }
}
