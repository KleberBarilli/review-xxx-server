package com.idealizer.review_x.domain.activity.like.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "likes")
@CompoundIndexes({
        @CompoundIndex(
                name = "uq_target_user",
                def = "{ 'target_type': 1, 'target_id': 1, 'user_id': 1 }",
                unique = true
        ),
        @CompoundIndex(
                name = "by_user_createdAt",
                def = "{ 'user_id': 1, 'created_at': -1 }"
        ),
        @CompoundIndex(
                name = "by_target",
                def = "{ 'target_type': 1, 'target_id': 1 }"
        )
})
public class Like {
    @Id
    private ObjectId id;
    @Field(value = "user_id")
    private ObjectId userId;
    @Field(value = "target_id")
    private ObjectId targetId;
    @Field(value = "target_type")
    private LikeType targetType;

    @Field("created_at")
    @CreatedDate
    private Instant createdAt;

    public Like(ObjectId userId, ObjectId targetId, LikeType targetType) {
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getTargetId() {
        return targetId;
    }

    public void setTargetId(ObjectId targetId) {
        this.targetId = targetId;
    }

    public LikeType getTargetType() {
        return targetType;
    }

    public void setTargetType(LikeType targetType) {
        this.targetType = targetType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
