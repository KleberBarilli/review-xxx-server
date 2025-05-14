package com.idealizer.review_x.modules.books.controllers;

import com.idealizer.review_x.common.dto.ResponseError;
import com.idealizer.review_x.exceptions.DuplicatedException;
import com.idealizer.review_x.modules.books.controllers.dto.CreateBookDTO;
import com.idealizer.review_x.modules.books.services.CreateBookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private  CreateBookService createBookService;

    public BookController (CreateBookService createBookService) {
        this.createBookService = createBookService;
    }

    @PostMapping
    public ResponseEntity<Object> createBook(@RequestBody @Valid CreateBookDTO dto) {
        try {
            return ResponseEntity.ok(dto);
        } catch (DuplicatedException e) {
            var errorDto = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(errorDto.status()).body(errorDto);
        }
    }

}
