package com.idealizer.review_x.application.review.usecases;

import com.idealizer.review_x.common.dtos.PageResponse;
import com.idealizer.review_x.common.dtos.review.FindReviewDTO;
import com.idealizer.review_x.domain.core.review.interfaces.BaseReview;
import com.idealizer.review_x.domain.core.review.repositories.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class FindReviewByUsernameUseCase {
    private final ReviewRepository reviewRepository;

    public FindReviewByUsernameUseCase(
            ReviewRepository reviewRepository
    ) {
        this.reviewRepository = reviewRepository;
    }

    public PageResponse<BaseReview> execute(String username, FindReviewDTO f) {
        int page = Math.max(f.pageNumber(), 0);
        int size = Math.max(f.limit(), 1);

        Page<BaseReview> r = reviewRepository.searchProjected(
                username, f, PageRequest.of(page, size)
        );

        return new PageResponse<>(
                page,
                size,
                r.getTotalElements(),
                r.getTotalPages(),
                r.isLast(),
                r.getContent()
        );
    }
}
