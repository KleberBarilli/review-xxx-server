package com.idealizer.review_x.application.user.responses;

import java.util.List;

public record AccessTokenResponse(
        String access,
        long expiresInSeconds,
        User user
) {
    public record User(String id, String username, List<String> roles) {}
}
