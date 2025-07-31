package com.idealizer.review_x.application.games.profile.review.commands;

import org.bson.types.ObjectId;

import java.time.Instant;

public class CreateUpdateReviewCommand {

    private ObjectId userId;
    private ObjectId gameId;
    private ObjectId profileGameId;
    private String title;
    private String content;
    private Boolean spoiler;
    private Boolean replay;
    private Instant startedAt;
    private Instant finishedAt;

    private String gameName;
    private String gameSlug;
    private String gameCover;

    public CreateUpdateReviewCommand() {
    }

    public CreateUpdateReviewCommand(ObjectId userId, ObjectId gameId, ObjectId profileGameId, String title, String content,
                                     Boolean spoiler, Boolean replay, Instant startedAt, Instant finishedAt,
                                     String gameName, String gameSlug, String gameCover
    ) {
        this.userId = userId;
        this.gameId = gameId;
        this.profileGameId = profileGameId;
        this.title = title;
        this.content = content;
        this.spoiler = spoiler;
        this.replay = replay;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.gameName = gameName;
        this.gameSlug = gameSlug;
        this.gameCover = gameCover;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getGameId() {
        return gameId;
    }

    public void setGameId(ObjectId gameId) {
        this.gameId = gameId;
    }

    public ObjectId getProfileGameId() {
        return profileGameId;
    }

    public void setProfileGameId(ObjectId profileGameId) {
        this.profileGameId = profileGameId;
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameSlug() {
        return gameSlug;
    }

    public void setGameSlug(String gameSlug) {
        this.gameSlug = gameSlug;
    }

    public String getGameCover() {
        return gameCover;
    }

    public void setGameCover(String gameCover) {
        this.gameCover = gameCover;
    }
}
