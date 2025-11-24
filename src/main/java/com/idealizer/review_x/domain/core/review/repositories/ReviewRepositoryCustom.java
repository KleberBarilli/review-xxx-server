package com.idealizer.review_x.domain.core.review.repositories;

import com.idealizer.review_x.common.dtos.review.FindReviewDTO;
import com.idealizer.review_x.domain.core.review.interfaces.BaseReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<BaseReview> searchProjected(String username, FindReviewDTO f, Pageable pageable);
}