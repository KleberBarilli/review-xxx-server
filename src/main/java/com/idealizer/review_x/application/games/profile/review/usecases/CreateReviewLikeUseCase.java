package com.idealizer.review_x.application.games.profile.review.usecases;

import com.idealizer.review_x.domain.profile.game.entities.LikeType;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import com.idealizer.review_x.domain.user.entities.Like;
import com.idealizer.review_x.domain.user.repositories.LikeRepository;
import com.mongodb.DuplicateKeyException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateReviewLikeUseCase {

    public final LikeRepository likeRepository;
    public final ProfileReviewRepository profileReviewRepository;

    public CreateReviewLikeUseCase(LikeRepository likeRepository, ProfileReviewRepository profileReviewRepository) {
        this.likeRepository = likeRepository;
        this.profileReviewRepository = profileReviewRepository;
    }

    @Transactional
    public void execute(ObjectId reviewId, ObjectId userId) {

        try {
            likeRepository.insert(new Like(userId, reviewId, null, LikeType.REVIEW));
            profileReviewRepository.findAndModifyById(reviewId, +1);

        } catch (DuplicateKeyException ignore) {

        }

    }
}
