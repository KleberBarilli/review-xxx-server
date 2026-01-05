package com.idealizer.review_x.application.user.usecases;

import com.idealizer.review_x.application.review.responses.LastReviewItemResponse;
import com.idealizer.review_x.application.review.usecases.FindLastReviewsByUserUseCase;
import com.idealizer.review_x.application.user.mappers.UserMapper;
import com.idealizer.review_x.application.user.responses.FindUserResponse;
import com.idealizer.review_x.common.dtos.FindUserArgsDTO;
import com.idealizer.review_x.domain.core.activity.follow.repositories.FollowRepository;
import com.idealizer.review_x.domain.core.profile.game.interfaces.SimpleProfileGame;
import com.idealizer.review_x.domain.core.profile.game.repositories.ProfileGameRepository;
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindUserByNameUseCase {
    private static final Logger log = LoggerFactory.getLogger(FindUserByNameUseCase.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileGameRepository profileGameRepository;
    private final FindLastReviewsByUserUseCase findLastReviewsByUserUseCase;
    private final FollowRepository followRepository;

    public FindUserByNameUseCase(
            UserRepository userRepository, UserMapper userMapper,
            ProfileGameRepository profileGameRepository, FindLastReviewsByUserUseCase findLastReviewsByUserUseCase,
            FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.profileGameRepository = profileGameRepository;
        this.findLastReviewsByUserUseCase = findLastReviewsByUserUseCase;
        this.followRepository = followRepository;

    }

    public FindUserResponse execute(String name, FindUserArgsDTO args, ObjectId userLoggedId) {

        FindUserResponse user = userRepository.findByName(name)
                .map(userMapper::toDetailedUser)
                .orElse(null);
        if (user == null)
            return null;

        log.info(userLoggedId.toString());
        if(userLoggedId != null){
             ObjectId userTargetId = new ObjectId(user.getId());
             user.setIsFollowing(this.followRepository.existsByFollowerIdAndFolloweeId(userLoggedId, userTargetId));
             user.setIsMe(userTargetId.equals(userLoggedId));
        }

        ObjectId userId = new ObjectId(user.getId());
        if (args.favorite()) {
            List<SimpleProfileGame> favoriteGames =
                    profileGameRepository.findProjectedByUserIdAndFavoriteIsTrue(userId);
            user.setFavoriteGames(favoriteGames);
        }
        if (args.lastReviews()) {
            List<LastReviewItemResponse> lastReviews =
                    findLastReviewsByUserUseCase.execute(userId);
            user.setLastReviews(lastReviews);
        }
        if(args.mastered()){
            List<SimpleProfileGame> masteredGames = profileGameRepository.findProjectedByUserIdAndMasteredIsTrue(userId);
            user.setMasteredGames(masteredGames);
        }
        if(args.playing()){
            List<SimpleProfileGame> playingGames = profileGameRepository.findProjectedByUserIdAndPlayingIsTrue(userId);
            user.setPlayingGames(playingGames);
        }
        return user;
    }
}
