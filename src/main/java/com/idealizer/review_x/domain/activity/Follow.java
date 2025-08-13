package com.idealizer.review_x.domain.activity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "follows")
@CompoundIndex(name = "uniq_follow", def = "{'followerId':1,'followingId':1}", unique = true)
public class Follow {
    @Id
    private ObjectId id;
    @Field(value = "follower_id")
    private ObjectId followerId;
    @Field(value = "following_id")
    private ObjectId followingId;
    @Field(value = "created_at")
    @CreatedDate
    private Instant createdAt;

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

    public ObjectId getFollowingId() {
        return followingId;
    }

    public void setFollowingId(ObjectId followingId) {
        this.followingId = followingId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
