package com.idealizer.review_x.modules.books.controllers.dto;

import java.time.LocalDate;

import org.bson.types.ObjectId;

public record AuthorDTO(
                ObjectId id,
                String name,
                LocalDate birthDate,
                String avatarUrl,
                String nationality

) {
}
