package com.idealizer.review_x.domain.activity.follow.repositories;

import com.idealizer.review_x.domain.activity.follow.entities.Follow;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FollowRepository extends MongoRepository<Follow, ObjectId> {
    boolean existsByFollowerIdAndFolloweeId(ObjectId followerId, ObjectId followeeId);
    Optional<Follow> findByFollowerIdAndFolloweeId(ObjectId followerId, ObjectId followeeId);

    Page<Follow> findAllByFollowerId(ObjectId followerId, Pageable pageable);
    Page<Follow> findAllByFolloweeId(ObjectId followeeId, Pageable pageable);

    long deleteByFollowerIdAndFolloweeId(ObjectId followerId, ObjectId followeeId);
}
