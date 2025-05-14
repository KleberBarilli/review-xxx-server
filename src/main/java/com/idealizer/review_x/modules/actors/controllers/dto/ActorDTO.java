package com.idealizer.review_x.modules.actors.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.idealizer.review_x.Person;
import com.idealizer.review_x.modules.actors.entities.Actor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ActorDTO(UUID id,
                       @NotBlank
                       @Size(min = 3, max = 150)
                       String name,
                       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                       @Past(message = "Birth date must be in the past")
                       LocalDate birthDate,
                       String avatarUrl,
                       String description,
                       @NotBlank
                       @Size(min = 3, max = 50)
                       String nationality, LocalDateTime createdAt, LocalDateTime updatedAt) {
}


