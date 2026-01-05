package com.idealizer.review_x.common.dtos;

import java.util.List;

public record FindTimelineDTO(
        int pageNumber,
        int limit,
        String sort,
        String order,
        Integer year,
        List<String> types,
        boolean onlyReview
) {}