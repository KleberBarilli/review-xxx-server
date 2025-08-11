package com.idealizer.review_x.domain.user.repositories;

import com.idealizer.review_x.domain.user.entities.Follow;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRepository {
    boolean existsByFollowerIdAndFollowingId(ObjectId followerId, ObjectId followingId);
    void deleteByFollowerIdAndFollowingId(ObjectId followerId, ObjectId followingId);
    Page<Follow> findByFollowerId(ObjectId followerId, Pageable pageable);
    Page<Follow> findByFollowingId(ObjectId followingId, Pageable pageable);
    long countByFollowerId(ObjectId followerId);
    long countByFollowingId(ObjectId followingId);
}
