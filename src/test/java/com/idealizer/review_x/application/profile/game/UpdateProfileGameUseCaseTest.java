package com.idealizer.review_x.application.profile.game;

import com.idealizer.review_x.application.games.profile.game.usecases.UpdateProfileGameUseCase;
import com.idealizer.review_x.domain.profile.game.ProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
public class UpdateProfileGameUseCaseTest {

    private ProfileGameRepository profileGameRepository;
    private UpdateProfileGameUseCase updateProfileGameUseCase;

    @BeforeEach
    void setUp() {
        profileGameRepository = mock(ProfileGameRepository.class);
        updateProfileGameUseCase = new UpdateProfileGameUseCase(profileGameRepository);
    }

    @Test
    void shouldUpdateProfileGame() {
        ProfileGame profileGame = new ProfileGame();
        profileGame.setId(new ObjectId());

        updateProfileGameUseCase.execute(profileGame);
        verify(profileGameRepository, times(1)).save(profileGame);
    }
}
