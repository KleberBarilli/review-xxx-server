package com.idealizer.review_x.modules.books.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.idealizer.review_x.modules.books.BookGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record CreateBookDTO(
        @NotBlank(message = "ISBN is required")
        String isbn,
        @NotBlank (message = "Title is required")
        String title,
        @NotNull(message = "Publication date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @Past(message = "Publication date must be in the past")
        LocalDate publicationAt,
        Set<BookGenre> genres,
        String alternativeTitles,
        @NotNull(message = "Author is required")
        UUID authorId
                            ) {
}
