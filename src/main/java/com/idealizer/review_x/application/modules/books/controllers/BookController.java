package com.idealizer.review_x.application.modules.books.controllers;

import com.idealizer.review_x.common.controller.GenericController;
import com.idealizer.review_x.application.modules.books.Book;
import com.idealizer.review_x.application.modules.books.controllers.dto.CreateBookDTO;
import com.idealizer.review_x.application.modules.books.controllers.mappers.BookMapper;
import com.idealizer.review_x.application.modules.books.services.CreateBookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController implements GenericController {

    private final BookMapper bookMapper;
    private final CreateBookService createBookService;

    public BookController(CreateBookService createBookService, BookMapper bookMapper) {
        this.createBookService = createBookService;
        this.bookMapper = bookMapper;
    }

    @PostMapping
    public ResponseEntity<Object> createBook(@RequestBody @Valid CreateBookDTO dto) {
        Book book = bookMapper.toEntity(dto);
        var url = getLocationHeader(book.getId());
        return ResponseEntity.created(url).build();
    }

}
