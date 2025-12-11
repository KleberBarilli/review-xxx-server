package com.idealizer.review_x.infra.http.controllers.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUsernameDTO(
        @Schema(description = "New unique username", example = "dev_master", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Username is required.")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
        @Pattern(
                regexp = "^[a-zA-Z0-9_.]+$",
                message = "Username must contain only letters, numbers, dots (.), and underscores (_)."
        )
        String name
) {
    public UpdateUsernameDTO {
        if (name != null) {
            name = name.trim();
        }
    }
}