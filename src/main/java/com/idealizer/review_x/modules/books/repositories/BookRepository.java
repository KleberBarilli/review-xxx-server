package com.idealizer.review_x.modules.books.repositories;

import com.idealizer.review_x.modules.books.Author;
import com.idealizer.review_x.modules.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    List<Book> findByAuthor(Author author);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAlternativeTitles(String altTitle);

}
