package com.idealizer.review_x.application.activity.follow.usecases;

import com.idealizer.review_x.common.exceptions.DuplicatedException;
import com.idealizer.review_x.domain.activity.follow.entities.Follow;
import com.idealizer.review_x.domain.activity.follow.repositories.FollowRepository;
import com.idealizer.review_x.domain.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowUseCase {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowUseCase(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(ObjectId userId, ObjectId followedId) {
        if (userId.equals(followedId)) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }


        boolean created = false;
        try {
            followRepository.insert(new Follow(userId, followedId));
            created = true;
        } catch (DuplicatedException ignore) {
        }

        if (created) {
            userRepository.incFollowing(userId, +1L);
            userRepository.incFollowers(followedId, +1L);
        }
    }
}
