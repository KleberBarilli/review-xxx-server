package com.idealizer.review_x.application.activity.follow.usecases;

import com.idealizer.review_x.domain.activity.follow.repositories.FollowRepository;
import com.idealizer.review_x.domain.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UnfollowUseCase {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public UnfollowUseCase(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(ObjectId actorId, ObjectId targetId) {
        if (actorId.equals(targetId)) return;

        long deleted = followRepository.deleteByFollowerIdAndFolloweeId(actorId, targetId);
        if (deleted > 0) {
            userRepository.incFollowing(actorId, -1L);
            userRepository.incFollowers(targetId, -1L);
        }
    }
}
