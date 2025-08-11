package com.idealizer.review_x.application.games.profile.review.usecases;

import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import com.idealizer.review_x.domain.user.repositories.LikeRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteReviewLikeUseCase {

    public final LikeRepository likeRepository;
    public final ProfileReviewRepository profileReviewRepository;

    public DeleteReviewLikeUseCase(LikeRepository likeRepository, ProfileReviewRepository profileReviewRepository) {
        this.likeRepository = likeRepository;
        this.profileReviewRepository = profileReviewRepository;
    }

    @Transactional
    public void execute(ObjectId reviewId, ObjectId userId) {

        long deleted = likeRepository.deleteByReviewIdAndUserId(reviewId, userId);
        if (deleted == 1) profileReviewRepository.findAndModifyById(reviewId, -1);

    }
}
