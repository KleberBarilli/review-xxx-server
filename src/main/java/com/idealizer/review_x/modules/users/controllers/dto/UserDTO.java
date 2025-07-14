package com.idealizer.review_x.modules.users.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Schema(name = "User")
public record UserDTO(
                ObjectId id,
                @NotBlank @Size(min = 3, max = 100) String name,
                @NotBlank @Email(message = "Invalid email format") String email,
                @URL(message = "Invalid URL format") String avatarUrl,
                @URL(message = "Invalid URL format") String letterboxdUrl,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
