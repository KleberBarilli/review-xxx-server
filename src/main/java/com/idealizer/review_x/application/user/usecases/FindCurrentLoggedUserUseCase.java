package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.user.mappers.UserMapper;
import com.idealizer.review_x.application.user.responses.CurrentLoggedUserResponse;
import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindCurrentLoggedUserUseCase {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileGameRepository profileGameRepository;

    public FindCurrentLoggedUserUseCase(UserRepository userRepository, UserMapper userMapper,
                                        ProfileGameRepository profileGameRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.profileGameRepository = profileGameRepository;
    }

    public CurrentLoggedUserResponse execute(ObjectId userId) {

        List<SimpleProfileGame> favoriteGames = profileGameRepository.findProjectedByUserIdAndFavoriteIsTrue(userId);

        CurrentLoggedUserResponse response = userRepository.findById(userId)
                .map(userMapper::toCurrentLoggedUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        response.setFavoriteGames(favoriteGames);

        return response;
    }
}
