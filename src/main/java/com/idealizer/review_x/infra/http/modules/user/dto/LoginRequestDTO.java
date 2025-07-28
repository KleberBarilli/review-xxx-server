package com.idealizer.review_x.infra.http.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDTO(
        @Schema(defaultValue = "username123")
        String username,
        @Schema(defaultValue = "password123")
        String password,
        @Schema(defaultValue = "en")
        String locale
) {
}
