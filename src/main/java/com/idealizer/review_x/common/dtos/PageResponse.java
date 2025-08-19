package com.idealizer.review_x.common.dtos;

import java.util.List;

public record PageResponse<T>(
        int pageNumber,
        int limit,
        long totalElements,
        int totalPages,
        boolean last,
        List<T> data
) {}
