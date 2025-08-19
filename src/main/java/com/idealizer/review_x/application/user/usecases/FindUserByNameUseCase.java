package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.review.responses.LastReviewItemResponse;
import com.idealizer.review_x.application.review.usecases.FindLastReviewsUseCase;
import com.idealizer.review_x.application.user.mappers.UserMapper;
import com.idealizer.review_x.application.user.responses.FindUserResponse;
import com.idealizer.review_x.common.dtos.FindUserArgsDTO;
import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindUserByNameUseCase {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileGameRepository profileGameRepository;
    private final FindLastReviewsUseCase findLastReviewsUseCase;

    public FindUserByNameUseCase(
            UserRepository userRepository, UserMapper userMapper,
            ProfileGameRepository profileGameRepository, FindLastReviewsUseCase findLastReviewsUseCase) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.profileGameRepository = profileGameRepository;
        this.findLastReviewsUseCase = findLastReviewsUseCase;

    }

    public FindUserResponse execute(String name, FindUserArgsDTO args) {

        FindUserResponse user = userRepository.findByName(name)
                .map(userMapper::toDetailedUser)
                .orElse(null);
        if (user == null)
            return null;


        ObjectId userId = new ObjectId(user.getId());

        if (args.favorite()) {
            List<SimpleProfileGame> favoriteGames =
                    profileGameRepository.findProjectedByUserIdAndFavoriteIsTrue(userId);
            user.setFavoriteGames(favoriteGames);
        }

        if (args.lastReviews()) {
            List<LastReviewItemResponse> lastReviews =
                    findLastReviewsUseCase.execute(userId);
            user.setLastReviews(lastReviews);
        }

        return user;
    }
}
