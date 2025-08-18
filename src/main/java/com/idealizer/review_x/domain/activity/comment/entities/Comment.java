package com.idealizer.review_x.domain.activity.comment.entities;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "comments")
@CompoundIndexes({
        @CompoundIndex(name = "comments_by_target_created",
                def = "{ 'target_type': 1, 'target_id': 1, 'created_at': 1 }"),
        @CompoundIndex(name = "comments_by_user_created",
                def = "{ 'user_id': 1, 'created_at': -1 }")
})

public class Comment {
    @Id
    private ObjectId id;
    @Field(value = "user_id")
    private ObjectId userId;
    @Field(value = "full_name")
    private String fullName;
    private String username;
    @Field(value = "target_id")
    private ObjectId targetId;
    @Field(value = "target_type")
    private CommentType targetType;
    private String content;
    private Boolean spoiler;

    @Field(value = "like_count")
    private long likeCount = 0L;


    @Field(value = "created_at")
    @CreatedDate
    private Instant createdAt;
    @Field(value = "edited_at")
    private Instant editedAt;
    @Field(value = "deleted_at")
    private Instant deletedAt;

    public Comment() {

    }

    public Comment(ObjectId id, ObjectId userId, String username, String fullName, ObjectId targetId, CommentType targetType, String content, Boolean spoiler, Integer likeCount, Instant createdAt, Instant editedAt, Instant deletedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.targetId = targetId;
        this.targetType = targetType;
        this.content = content;
        this.spoiler = spoiler;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.deletedAt = deletedAt;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ObjectId getTargetId() {
        return targetId;
    }

    public void setTargetId(ObjectId targetId) {
        this.targetId = targetId;
    }

    public CommentType getTargetType() {
        return targetType;
    }

    public void setTargetType(CommentType targetType) {
        this.targetType = targetType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSpoiler() {
        return spoiler;
    }

    public void setSpoiler(Boolean spoiler) {
        this.spoiler = spoiler;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Instant editedAt) {
        this.editedAt = editedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
