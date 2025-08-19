package com.idealizer.review_x.application.review.usecases;

import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.common.exceptions.ForbiddenException;
import com.idealizer.review_x.domain.review.repositories.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.logging.Logger;

@Service
public class DeleteReviewUseCase {
    private final Logger logger = Logger.getLogger(DeleteReviewUseCase.class.getName());
    private final ReviewRepository reviewRepository;
private final MessageUtil messageUtil;

    public DeleteReviewUseCase(ReviewRepository reviewRepository, MessageUtil messageUtil) {
        this.reviewRepository = reviewRepository;
        this.messageUtil = messageUtil;
    }

    public void execute(ObjectId userId, ObjectId reviewId) {
        Locale locale = LocaleContextHolder.getLocale();

        long deletedCount = reviewRepository.deleteByIdAndUserId(reviewId, userId);

        if (deletedCount == 0) {
            throw new ForbiddenException(messageUtil.get("review.delete.error", null, locale));
        }
        logger.info("Review deleted successfully: " + reviewId);

    }
}
