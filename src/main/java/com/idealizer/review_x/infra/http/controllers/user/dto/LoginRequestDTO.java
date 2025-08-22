package com.idealizer.review_x.infra.http.controllers.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDTO(
        @Schema(defaultValue = "username123|user@gmail.com")
        String identifier,
        @Schema(defaultValue = "password123")
        String password
) {
}
