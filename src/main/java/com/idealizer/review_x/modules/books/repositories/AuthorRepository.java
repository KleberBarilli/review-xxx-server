package com.idealizer.review_x.modules.books.repositories;

import com.idealizer.review_x.modules.books.Author;
import com.idealizer.review_x.modules.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuthorRepository  extends JpaRepository<Author, UUID> {


}
