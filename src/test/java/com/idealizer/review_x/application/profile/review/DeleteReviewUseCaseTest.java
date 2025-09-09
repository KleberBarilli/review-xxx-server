package com.idealizer.review_x.application.profile.review;

import com.idealizer.review_x.application.review.usecases.DeleteReviewUseCase;
import com.idealizer.review_x.common.MessageUtil;
import com.idealizer.review_x.common.exceptions.ForbiddenException;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteReviewUseCaseTest {
    private ReviewRepository reviewRepository;
    private MessageUtil messageUtil;
    private DeleteReviewUseCase deleteReviewUseCase;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        messageUtil = mock(MessageUtil.class);
        deleteReviewUseCase = new DeleteReviewUseCase(reviewRepository, messageUtil);
    }

    @Test
    void shouldDeleteReviewSuccessfully() {
        ObjectId userId = new ObjectId();
        ObjectId reviewId = new ObjectId();

        when(reviewRepository.deleteByIdAndUserId(reviewId, userId)).thenReturn(1L);

        assertDoesNotThrow(() -> deleteReviewUseCase.execute(userId, reviewId));
        verify(reviewRepository, times(1)).deleteByIdAndUserId(reviewId, userId);
    }

    @Test
    void shouldThrowForbiddenExceptionWhenDeleteFails() {
        ObjectId userId = new ObjectId();
        ObjectId reviewId = new ObjectId();
        Locale locale = LocaleContextHolder.getLocale();

        when(reviewRepository.deleteByIdAndUserId(reviewId, userId)).thenReturn(0L);
        when(messageUtil.get(eq("review.delete.error"), isNull(), eq(locale))).thenReturn("You are not allowed to delete this review");

        ForbiddenException exception = assertThrows(ForbiddenException.class, () ->
                deleteReviewUseCase.execute(userId, reviewId)
        );

        assertEquals("You are not allowed to delete this review", exception.getMessage());
        verify(reviewRepository, times(1)).deleteByIdAndUserId(reviewId, userId);
    }
}

