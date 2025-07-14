package com.idealizer.review_x.modules.books.controllers.dto;

import com.idealizer.review_x.modules.books.BookGenre;

import java.time.LocalDate;
import java.util.Set;

import org.bson.types.ObjectId;

public record FindBookDTO(
                ObjectId id,
                String isbn,
                String title,
                LocalDate publicationAt,
                Set<BookGenre> genres,
                String alternativeTitles,
                AuthorDTO author) {
}