package com.idealizer.review_x.domain.core.user.repositories;

import com.idealizer.review_x.domain.core.user.entities.User;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    boolean existsByEmail(String email);
    boolean existsByName(String username);

    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByEmailOrName(String email, String name);


    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'followingCount': ?1 } }")
    long incFollowing(ObjectId userId, long delta);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'followerCount': ?1 } }")
    long incFollowers(ObjectId userId, long delta);

    @Query(value = "{ '_id' : { $in : ?0 } }", fields = "{ 'name': 1, 'full_name': 1, 'avatar_url': 1, 'bio': 1 }")
    List<User> findFromListOptimized(List<ObjectId> ids);

    @Query("{ '$or': [ { 'name': { '$regex': ?0, '$options': 'i' } }, { 'fullName': { '$regex': ?0, '$options': 'i' } } ] }")
    List<User> searchByTerm(String term, Pageable pageable);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'name': ?1 } }")
    long updateNameById(ObjectId userId, String newName);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'last_login' : ?1 } }")
    void updateLastLogin(ObjectId id, Instant lastLogin);

}
