package com.idealizer.review_x.modules.books.repositories;

import com.idealizer.review_x.modules.books.Author;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<Author, ObjectId> {

}
