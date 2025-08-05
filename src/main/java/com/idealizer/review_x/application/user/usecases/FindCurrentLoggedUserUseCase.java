package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.games.profile.review.responses.LastReviewItemResponse;
import com.idealizer.review_x.application.games.profile.review.usecases.FindLastReviewsUseCase;
import com.idealizer.review_x.application.user.mappers.UserMapper;
import com.idealizer.review_x.application.user.responses.CurrentLoggedUserResponse;
import com.idealizer.review_x.common.dtos.CurrentLoggedUserArgsDTO;
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
    private final FindLastReviewsUseCase findLastReviewsUseCase;

    public FindCurrentLoggedUserUseCase(UserRepository userRepository, UserMapper userMapper,
                                        ProfileGameRepository profileGameRepository, FindLastReviewsUseCase findLastReviewsUseCase) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.profileGameRepository = profileGameRepository;
        this.findLastReviewsUseCase = findLastReviewsUseCase;

    }

    public CurrentLoggedUserResponse execute(ObjectId userId, CurrentLoggedUserArgsDTO args) {

        CurrentLoggedUserResponse response = userRepository.findById(userId)
                .map(userMapper::toCurrentLoggedUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (args.favorite()) {
            List<SimpleProfileGame> favoriteGames =
                    profileGameRepository.findProjectedByUserIdAndFavoriteIsTrue(userId);
            response.setFavoriteGames(favoriteGames);
        }

        if (args.lastReviews()) {
            List<LastReviewItemResponse> lastReviews =
                    findLastReviewsUseCase.execute(userId);
            response.setLastReviews(lastReviews);
        }

        return response;
    }
}
