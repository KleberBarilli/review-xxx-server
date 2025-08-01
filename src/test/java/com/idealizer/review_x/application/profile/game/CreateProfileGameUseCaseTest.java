package com.idealizer.review_x.application.profile.game;

import com.idealizer.review_x.application.games.profile.game.commands.CreateUpdateProfileGameCommand;
import com.idealizer.review_x.application.games.profile.game.mappers.ProfileGameMapper;
import com.idealizer.review_x.application.games.profile.game.usecases.CreateProfileGameUseCase;
import com.idealizer.review_x.domain.profile.game.ProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateProfileGameUseCaseTest {
    private ProfileGameRepository profileGameRepository;
    private ProfileGameMapper profileGameMapper;
    private CreateProfileGameUseCase createProfileGameUseCase;

    @BeforeEach
    void setUp() {
        profileGameRepository = mock(ProfileGameRepository.class);
        profileGameMapper = mock(ProfileGameMapper.class);
        createProfileGameUseCase = new CreateProfileGameUseCase(profileGameRepository, profileGameMapper);
    }

    @Test
    void shouldCreateProfileGameAndReturnId() {
        CreateUpdateProfileGameCommand command = new CreateUpdateProfileGameCommand();
        ProfileGame profileGame = new ProfileGame();
        ObjectId generatedId = new ObjectId();
        profileGame.setId(generatedId);

        when(profileGameMapper.toEntity(command)).thenReturn(profileGame);
        when(profileGameRepository.save(profileGame)).thenReturn(profileGame);

        ObjectId result = createProfileGameUseCase.execute(command);

        assertNotNull(result);
        assertEquals(generatedId, result);
        verify(profileGameMapper, times(1)).toEntity(command);
        verify(profileGameRepository, times(1)).save(profileGame);
    }
}
