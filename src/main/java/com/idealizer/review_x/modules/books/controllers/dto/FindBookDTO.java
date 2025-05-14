package com.idealizer.review_x.modules.books.controllers.dto;

import com.idealizer.review_x.modules.books.BookGenre;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;



public record FindBookDTO(
        UUID id,
        String isbn,
        String title,
        LocalDate publicationAt,
        Set<BookGenre> genres,
        String alternativeTitles,
        AuthorDTO author
) {
}