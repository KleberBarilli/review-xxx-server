package com.idealizer.review_x.domain.user.repositories;

import com.idealizer.review_x.domain.user.entities.User;

import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByEmailOrName(String email, String name);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'followingCount': ?1 } }")
    long incFollowing(ObjectId userId, long delta);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'followerCount': ?1 } }")
    long incFollowers(ObjectId userId, long delta);

}
