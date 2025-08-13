package com.idealizer.review_x.application.activity.like.usecases;

import com.idealizer.review_x.application.activity.like.services.LikeCounterRegistryService;
import com.idealizer.review_x.domain.activity.like.entities.LikeType;
import com.idealizer.review_x.domain.activity.like.repositories.LikeRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;


@Service
public class UnlikeUseCase {
    private final LikeRepository likeRepository;
    private final LikeCounterRegistryService counters;

    public UnlikeUseCase(LikeRepository likeRepository,
                             LikeCounterRegistryService counters) {
        this.likeRepository = likeRepository;
        this.counters = counters;
    }

    public void execute(ObjectId userId, ObjectId targetId, LikeType targetType) {
        long deleted = likeRepository
                .deleteByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);

        if (deleted == 1) {
            counters.get(targetType).inc(targetId, -1);
        }
    }
}