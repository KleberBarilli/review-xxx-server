package com.idealizer.review_x.domain.activity.follow.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "follows")
@CompoundIndexes({
        @CompoundIndex(name = "uniq_follower_followee", def = "{'followerId': 1, 'followeeId': 1}", unique = true),
        @CompoundIndex(name = "idx_follower_createdAt", def = "{'followerId': 1, 'createdAt': -1}"),
        @CompoundIndex(name = "idx_followee_createdAt", def = "{'followeeId': 1, 'createdAt': -1}")
})
public class Follow {
    @Id
    private ObjectId id;
    @Field(value = "follower_id")
    private ObjectId followerId;
    @Field(value = "followee_id")
    private ObjectId followeeId;
    @Field(value = "created_at")
    @CreatedDate
    private Instant createdAt;

    public Follow(ObjectId followerId, ObjectId followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getFollowerId() {
        return followerId;
    }

    public void setFollowerId(ObjectId followerId) {
        this.followerId = followerId;
    }

    public ObjectId getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(ObjectId followeeId) {
        this.followeeId = followeeId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
