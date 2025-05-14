package com.idealizer.review_x.modules.books.controllers.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorDTO(
        UUID id,
        String name,
        LocalDate birthDate,
        String avatarUrl,
        String nationality

) {
}
