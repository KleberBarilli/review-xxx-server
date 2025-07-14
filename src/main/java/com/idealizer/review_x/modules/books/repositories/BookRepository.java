package com.idealizer.review_x.modules.books.repositories;

import com.idealizer.review_x.modules.books.Book;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, ObjectId> {

    List<Book> findByAuthorId(String authorId);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAlternativeTitles(String altTitle);

}
