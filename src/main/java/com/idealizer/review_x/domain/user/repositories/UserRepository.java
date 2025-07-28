package com.idealizer.review_x.domain.user.repositories;

import com.idealizer.review_x.domain.user.entities.User;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByEmailOrName(String email, String name);

}
