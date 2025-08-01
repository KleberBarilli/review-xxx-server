package com.idealizer.review_x.application.games.profile.game.commands;

import com.idealizer.review_x.domain.game.entities.GamePlatform;
import com.idealizer.review_x.domain.profile.game.PlatformType;
import com.idealizer.review_x.domain.profile.game.ProfileGameStatus;
import org.bson.types.ObjectId;

import java.util.List;

public class CreateUpdateProfileGameCommand {
    private ObjectId userId;
    private ObjectId gameId;

    private PlatformType sourcePlatform;
    private GamePlatform playedOn;
    private Boolean hasReview;
    private ProfileGameStatus status;
    private Integer playtimeInMinutes;
    private Integer rating;
    private Boolean playing;
    private Boolean liked;
    private Boolean owned;
    private Boolean wishlist;
    private Boolean mastered;
    private List<String> favoriteScreenshots;

    private String gameName;
    private String gameSlug;
    private String gameCover;

    public CreateUpdateProfileGameCommand() {
    }

    public CreateUpdateProfileGameCommand(ObjectId userId, ObjectId gameId, PlatformType sourcePlatform,
                                          GamePlatform playedOn, Boolean hasReview,
                                          ProfileGameStatus status, Integer playtimeInMinutes, Integer rating,
                                          Boolean playing, Boolean liked, Boolean owned, Boolean wishlist,
                                          Boolean mastered, List<String> favoriteScreenshots,
                                          String gameName, String gameSlug, String gameCover
                                          ) {
        this.userId = userId;
        this.gameId = gameId;
        this.sourcePlatform = sourcePlatform;
        this.playedOn = playedOn;
        this.hasReview = hasReview;
        this.status = status;
        this.playtimeInMinutes = playtimeInMinutes;
        this.rating = rating;
        this.playing = playing;
        this.liked = liked;
        this.owned = owned;
        this.wishlist = wishlist;
        this.mastered = mastered;
        this.favoriteScreenshots = favoriteScreenshots;
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

    public Boolean getHasReview() {
        return hasReview;
    }

    public void setHasReview(Boolean hasReview) {
        this.hasReview = hasReview;
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
