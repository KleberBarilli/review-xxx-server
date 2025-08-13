package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.domain.profile.game.entities.ProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UpdateProfileGameUseCase {
    private final Logger logger = Logger.getLogger(UpdateProfileGameUseCase.class.getName());

    private final ProfileGameRepository profileGameRepository;

    public UpdateProfileGameUseCase(ProfileGameRepository profileGameRepository) {
        this.profileGameRepository = profileGameRepository;
    }

    public void execute(ProfileGame updatedProfileGame) {
        profileGameRepository.save(updatedProfileGame);
        logger.info("Profile Game updated successfully for profile-game ID: " + updatedProfileGame.getId());

    }
}
