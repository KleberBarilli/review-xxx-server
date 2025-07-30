package com.idealizer.review_x.application.profile.game.commands;

import com.idealizer.review_x.domain.game.entities.GamePlatform;
import com.idealizer.review_x.domain.profile.game.PlatformType;
import com.idealizer.review_x.domain.profile.game.ProfileGameStatus;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

public class UpsertProfileGameReviewCommand {
    private ObjectId userId;
    private ObjectId gameId;

    private PlatformType sourcePlatform;
    private GamePlatform playedOn;
    private String originalName;
    private String slug;
    private ProfileGameStatus status;
    private Integer playtimeInMinutes;
    private Integer rating;
    private Boolean playing;
    private Boolean favorite;
    private Boolean owned;
    private Boolean wishlist;
    private Boolean mastered;
    private List<String> favoriteScreenshots;


    private String reviewTitle;
    private String reviewContent;
    private Boolean reviewSpoiler;
    private Boolean reviewReplay;
    private Instant reviewReplayStartedAt;
    private Instant reviewFinishedAt;

    public UpsertProfileGameReviewCommand() {
    }

    public UpsertProfileGameReviewCommand(ObjectId userId, ObjectId gameId, PlatformType sourcePlatform, GamePlatform playedOn,
                                          String originalName, String slug, ProfileGameStatus status, Integer playtimeInMinutes,
                                          Integer rating, Boolean playing, Boolean favorite, Boolean owned, Boolean wishlist,
                                          Boolean mastered, List<String> favoriteScreenshots, String reviewTitle, String reviewContent,
                                          Boolean reviewSpoiler, Boolean reviewReplay, Instant reviewReplayStartedAt,
                                          Instant reviewFinishedAt) {
        this.userId = userId;
        this.gameId = gameId;
        this.sourcePlatform = sourcePlatform;
        this.playedOn = playedOn;
        this.originalName = originalName;
        this.slug = slug;
        this.status = status;
        this.playtimeInMinutes = playtimeInMinutes;
        this.rating = rating;
        this.playing = playing;
        this.favorite = favorite;
        this.owned = owned;
        this.wishlist = wishlist;
        this.mastered = mastered;
        this.favoriteScreenshots = favoriteScreenshots;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.reviewSpoiler = reviewSpoiler;
        this.reviewReplay = reviewReplay;
        this.reviewReplayStartedAt = reviewReplayStartedAt;
        this.reviewFinishedAt = reviewFinishedAt;
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

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
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

    public Instant getReviewReplayStartedAt() {
        return reviewReplayStartedAt;
    }

    public void setReviewReplayStartedAt(Instant reviewReplayStartedAt) {
        this.reviewReplayStartedAt = reviewReplayStartedAt;
    }

    public Instant getReviewFinishedAt() {
        return reviewFinishedAt;
    }

    public void setReviewFinishedAt(Instant reviewFinishedAt) {
        this.reviewFinishedAt = reviewFinishedAt;
    }
}
