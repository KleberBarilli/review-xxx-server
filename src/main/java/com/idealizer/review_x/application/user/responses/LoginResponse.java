package com.idealizer.review_x.application.user.responses;

import java.util.List;

public record LoginResponse(
        String userId,
        String username,
        List<String> roles,
        String accessToken,
        String refreshCookieValue,
        int refreshMaxAgeSeconds
) {}