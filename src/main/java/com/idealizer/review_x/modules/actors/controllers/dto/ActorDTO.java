package com.idealizer.review_x.modules.actors.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "Actor")
public record ActorDTO(ObjectId id,
        @NotBlank @Size(min = 3, max = 150) String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") @Past(message = "Birth date must be in the past") LocalDate birthDate,
        String avatarUrl,
        String description,
        @NotBlank @Size(min = 3, max = 50) String nationality, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
