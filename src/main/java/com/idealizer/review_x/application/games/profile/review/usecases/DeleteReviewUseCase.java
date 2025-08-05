package com.idealizer.review_x.application.games.profile.review.usecases;

import com.idealizer.review_x.application.games.profile.review.mappers.ReviewMapper;
import com.idealizer.review_x.common.LocaleUtil;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.common.exceptions.ForbiddenException;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.logging.Logger;

@Service
public class DeleteReviewUseCase {
    private final Logger logger = Logger.getLogger(DeleteReviewUseCase.class.getName());
    private final ProfileReviewRepository profileReviewRepository;
private final MessageUtil messageUtil;

    public DeleteReviewUseCase(ProfileReviewRepository profileReviewRepository, MessageUtil messageUtil) {
        this.profileReviewRepository = profileReviewRepository;
        this.messageUtil = messageUtil;
    }

    public void execute(ObjectId userId, ObjectId reviewId) {
        Locale locale = LocaleContextHolder.getLocale();

        long deletedCount = profileReviewRepository.deleteByIdAndUserId(reviewId, userId);

        if (deletedCount == 0) {
            throw new ForbiddenException(messageUtil.get("review.delete.error", null, locale));
        }
        logger.info("Review deleted successfully: " + reviewId);

    }
}
