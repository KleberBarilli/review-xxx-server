package com.idealizer.review_x.domain.review.entities;

import com.idealizer.review_x.domain.LogID;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "reviews")
@CompoundIndex(name = "user_target_idx", def = "{'user_id': 1, 'target_id': 1, 'target_type': 1}", unique = true)
public class Review {
    @Id
    private ObjectId id;

    @Field(value = "user_id")
    private ObjectId userId;
    private String username;
    @Field(value = "profile_target_id")
    private ObjectId profileTargetId;
    @Field(value = "target_id")
    private ObjectId targetId;

    @Field("target_type")
    private LogID targetType;

    private String title;
    private String content;
    private Boolean spoiler;
    private Boolean replay;

    @Field(value = "like_count")
    private long likeCount = 0L;

    @Field(value = "started_at")
    @PastOrPresent
    private Instant startedAt;
    @Field(value = "finished_at")
    @PastOrPresent
    private Instant finishedAt;
    @Field(value = "created_at")
    @CreatedDate
    private Instant createdAt;
    @Field(value = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    //duplicated fields below
    @Field(value = "target_name")
    private String targetName;
    @Field(value = "target_slug")
    private String targetSlug;
    @Field(value = "target_cover")
    private String targetCover;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Boolean getReplay() {
        return replay;
    }

    public void setReplay(Boolean replay) {
        this.replay = replay;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikes(long likeCount) {
        this.likeCount = likeCount;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ObjectId getProfileTargetId() {
        return profileTargetId;
    }

    public void setProfileTargetId(ObjectId profileTargetId) {
        this.profileTargetId = profileTargetId;
    }

    public ObjectId getTargetId() {
        return targetId;
    }

    public void setTargetId(ObjectId targetId) {
        this.targetId = targetId;
    }

    public LogID getTargetType() {
        return targetType;
    }

    public void setTargetType(LogID targetType) {
        this.targetType = targetType;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetSlug() {
        return targetSlug;
    }

    public void setTargetSlug(String targetSlug) {
        this.targetSlug = targetSlug;
    }

    public String getTargetCover() {
        return targetCover;
    }

    public void setTargetCover(String targetCover) {
        this.targetCover = targetCover;
    }
}
