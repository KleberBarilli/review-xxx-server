package com.idealizer.review_x.application.games.profile.game.commands;

import com.idealizer.review_x.domain.core.LogID;
import com.idealizer.review_x.domain.game.entities.enums.GamePlatform;
import com.idealizer.review_x.domain.core.profile.game.entities.PlatformType;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

public class UpsertProfileGameReviewCommand {
    private ObjectId userId;
    private ObjectId targetId;

    private PlatformType sourcePlatform;
    private GamePlatform playedOn;
    private String targetName;
    private String targetSlug;
    private ProfileGameStatus status;
    private Integer playtimeInMinutes;
    private Integer rating;
    private Boolean playing;
    private Boolean liked;
    private Boolean owned;
    private Boolean wishlist;
    private Boolean mastered;
    private List<String> favoriteScreenshots;
    private Instant startedAt;
    private Instant finishedAt;

    private String reviewTitle;
    private LogID reviewType;
    private String reviewContent;
    private Boolean reviewSpoiler;
    private Boolean reviewReplay;


    public UpsertProfileGameReviewCommand() {
    }

    public UpsertProfileGameReviewCommand(ObjectId userId, ObjectId targetId, PlatformType sourcePlatform,
            GamePlatform playedOn,
            String targetName, String targetSlug, ProfileGameStatus status, Integer playtimeInMinutes,
            Integer rating, Boolean playing, Boolean liked, Boolean owned, Boolean wishlist,
            Boolean mastered, List<String> favoriteScreenshots, String reviewTitle, LogID reviewType, String reviewContent,
            Boolean reviewSpoiler, Boolean reviewReplay, Instant startedAt,
            Instant finishedAt) {
        this.userId = userId;
        this.targetId = targetId;
        this.sourcePlatform = sourcePlatform;
        this.playedOn = playedOn;
        this.targetName = targetName;
        this.targetSlug = targetSlug;
        this.status = status;
        this.playtimeInMinutes = playtimeInMinutes;
        this.rating = rating;
        this.playing = playing;
        this.liked = liked;
        this.owned = owned;
        this.wishlist = wishlist;
        this.mastered = mastered;
        this.favoriteScreenshots = favoriteScreenshots;
        this.reviewTitle = reviewTitle;
        this.reviewType = reviewType;
        this.reviewContent = reviewContent;
        this.reviewSpoiler = reviewSpoiler;
        this.reviewReplay = reviewReplay;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }


    public PlatformType getSourcePlatform() {
        return sourcePlatform;
    }

    public void setSourcePlatform(PlatformType sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public GamePlatform getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(GamePlatform playedOn) {
        this.playedOn = playedOn;
    }


    public ObjectId getTargetId() {
        return targetId;
    }

    public void setTargetId(ObjectId targetId) {
        this.targetId = targetId;
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

    public ProfileGameStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileGameStatus status) {
        this.status = status;
    }

    public Integer getPlaytimeInMinutes() {
        return playtimeInMinutes;
    }

    public void setPlaytimeInMinutes(Integer playtimeInMinutes) {
        this.playtimeInMinutes = playtimeInMinutes;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getOwned() {
        return owned;
    }

    public void setOwned(Boolean owned) {
        this.owned = owned;
    }

    public Boolean getWishlist() {
        return wishlist;
    }

    public void setWishlist(Boolean wishlist) {
        this.wishlist = wishlist;
    }

    public Boolean getMastered() {
        return mastered;
    }

    public void setMastered(Boolean mastered) {
        this.mastered = mastered;
    }

    public List<String> getFavoriteScreenshots() {
        return favoriteScreenshots;
    }

    public void setFavoriteScreenshots(List<String> favoriteScreenshots) {
        this.favoriteScreenshots = favoriteScreenshots;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public Boolean getReviewSpoiler() {
        return reviewSpoiler;
    }

    public void setReviewSpoiler(Boolean reviewSpoiler) {
        this.reviewSpoiler = reviewSpoiler;
    }

    public Boolean getReviewReplay() {
        return reviewReplay;
    }

    public void setReviewReplay(Boolean reviewReplay) {
        this.reviewReplay = reviewReplay;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant StartedAt) {
        this.startedAt = StartedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public LogID getReviewType() {
        return reviewType;
    }

    public void setReviewType(LogID reviewType) {
        this.reviewType = reviewType;
    }
}
