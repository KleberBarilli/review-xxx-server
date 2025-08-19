package com.idealizer.review_x.application.review.commands;

import com.idealizer.review_x.domain.LogID;
import org.bson.types.ObjectId;

import java.time.Instant;

public class CreateUpdateReviewCommand {

    private ObjectId userId;
    private String username;
    private ObjectId targetId;
    private ObjectId profileTargetId;
    private String title;
    private LogID targetType;
    private String content;
    private Boolean spoiler;
    private Boolean replay;
    private Instant startedAt;
    private Instant finishedAt;

    private String targetName;
    private String targetSlug;
    private String targetCover;

    public CreateUpdateReviewCommand() {
    }

    public CreateUpdateReviewCommand(ObjectId userId, String username, ObjectId targetId, ObjectId targetProfileId, LogID targetType, String title, String content,
                                     Boolean spoiler, Boolean replay, Instant startedAt, Instant finishedAt,
                                     String targetName, String targetSlug, String targetCover
    ) {
        this.userId = userId;
        this.username = username;
        this.targetId = targetId;
        this.profileTargetId = targetProfileId;
        this.targetType = targetType;
        this.title = title;
        this.content = content;
        this.spoiler = spoiler;
        this.replay = replay;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.targetName = targetName;
        this.targetSlug = targetSlug;
        this.targetCover = targetCover;
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

    public ObjectId getTargetId() {
        return targetId;
    }

    public void setTargetId(ObjectId targetId) {
        this.targetId = targetId;
    }

    public ObjectId getProfileTargetId() {
        return profileTargetId;
    }

    public void setProfileTargetId(ObjectId profileTargetId) {
        this.profileTargetId = profileTargetId;
    }

    public LogID getTargetType() {
        return targetType;
    }

    public void setType(LogID targetType) {
        this.targetType = targetType;
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

    public void setTargetType(LogID targetType) {
        this.targetType = targetType;
    }
}
