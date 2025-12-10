package com.idealizer.review_x.application.user.responses;

public record SearchUserResponse(
        String id,
        String name,
        String fullName,
        String avatarUrl,
        String bio
) {}
