package com.idealizer.review_x.application.activity.follow.usecases;

import com.idealizer.review_x.common.dtos.activity.follow.FollowerResponse;
import com.idealizer.review_x.domain.core.activity.follow.entities.Follow;
import com.idealizer.review_x.domain.core.activity.follow.repositories.FollowRepository;
import com.idealizer.review_x.domain.core.user.entities.User;
import com.idealizer.review_x.domain.core.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FindFollowersUseCase {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FindFollowersUseCase(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public List<FollowerResponse> execute(ObjectId profileOwnerId, ObjectId viewerId) {
        List<Follow> follows = this.followRepository.findAllByFolloweeId(profileOwnerId);
        if (follows.isEmpty()) return Collections.emptyList();

        List<ObjectId> followerIds = follows.stream()
                .map(Follow::getFollowerId)
                .collect(Collectors.toList());

        List<User> users = this.userRepository.findFromListOptimized(followerIds);

        if (users.isEmpty()) {
            System.out.println("ERRO CRÍTICO: IDs existem no follow mas Users não retornaram. IDs: " + followerIds);
        }

        Map<ObjectId, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Set<ObjectId> idsIFollow = new HashSet<>();
        if (viewerId != null) {
            List<Follow> myFollows = this.followRepository.findByFollowerIdAndFolloweeIdIn(viewerId, followerIds);
            myFollows.forEach(f -> idsIFollow.add(f.getFolloweeId()));
        }

        return follows.stream()
                .map(follow -> {
                    User user = userMap.get(follow.getFollowerId());
                    if (user == null) return null;

                    boolean isFollowing = idsIFollow.contains(user.getId());
                    boolean isSameUser = viewerId != null && viewerId.equals(user.getId());

                    return new FollowerResponse(user, follow.getCreatedAt(), isFollowing, isSameUser);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}