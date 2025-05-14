package com.idealizer.review_x.modules.books.controllers.mappers;

import com.idealizer.review_x.modules.books.Book;
import com.idealizer.review_x.modules.books.controllers.dto.CreateBookDTO;
import com.idealizer.review_x.modules.books.repositories.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    @Autowired
    AuthorRepository authorRepository;


    @Mapping(target = "author", expression = "java(authorRepository.findById(dto.authorId()).orElse(null))")
    public abstract Book toEntity(CreateBookDTO dto);
}
