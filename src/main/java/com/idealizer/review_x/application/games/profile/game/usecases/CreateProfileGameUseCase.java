package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.application.games.profile.game.commands.CreateUpdateProfileGameCommand;
import com.idealizer.review_x.application.games.profile.game.mappers.ProfileGameMapper;
import com.idealizer.review_x.domain.profile.game.ProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CreateProfileGameUseCase {
    private static final Logger logger = Logger.getLogger(CreateProfileGameUseCase.class.getName());
    private final ProfileGameRepository profileGameRepository;
    private final ProfileGameMapper profileGameMapper;

    public CreateProfileGameUseCase(ProfileGameRepository profileGameRepository, ProfileGameMapper profileGameMapper) {

        this.profileGameRepository = profileGameRepository;
        this.profileGameMapper = profileGameMapper;
    }

    public ObjectId execute(CreateUpdateProfileGameCommand command) {
        ProfileGame profile =  profileGameRepository.save(profileGameMapper.toEntity(command));
        logger.info("Profile Game Created Successfully");
        return profile.getId();

    }

}
