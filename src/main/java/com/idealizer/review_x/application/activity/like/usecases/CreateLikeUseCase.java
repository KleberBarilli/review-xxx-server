package com.idealizer.review_x.application.activity.like.usecases;

import com.idealizer.review_x.application.activity.like.services.LikeCounterRegistryService;
import com.idealizer.review_x.domain.core.activity.like.entities.Like;
import com.idealizer.review_x.domain.core.activity.like.entities.LikeType;
import com.idealizer.review_x.domain.core.activity.like.repositories.LikeRepository;
import com.mongodb.DuplicateKeyException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;


@Service
public class CreateLikeUseCase {

    public final LikeRepository likeRepository;
    private final LikeCounterRegistryService likeCounterRegistryService;

    public CreateLikeUseCase(LikeRepository likeRepository, LikeCounterRegistryService likeCounterRegistryService) {
        this.likeRepository = likeRepository;
        this.likeCounterRegistryService = likeCounterRegistryService;
    }

    public void execute(ObjectId userId, ObjectId targetId, LikeType targetType) {

        try {

            likeRepository.insert(new Like(userId, targetId, targetType));
            likeCounterRegistryService.get(targetType).inc(targetId, +1);

        } catch (DuplicateKeyException ignore) {

        }

    }
}

