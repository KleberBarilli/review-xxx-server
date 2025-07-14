package com.idealizer.review_x.modules.users.repositories;

import com.idealizer.review_x.modules.users.entities.User;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByEmail(String email);
}
