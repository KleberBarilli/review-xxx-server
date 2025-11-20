package com.idealizer.review_x.domain.core.profile.game.entities;

import com.idealizer.review_x.domain.game.entities.enums.GamePlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Document(collection = "profile_games")
@CompoundIndexes({
    @CompoundIndex(
            name = "user_game_idx",
            def  = "{'user_id': 1, 'game_id': 1}",
            unique = true
    ),
    @CompoundIndex(
            name = "user_game_slug",
            def  = "{'username': 1, 'game_slug': 1}",
            unique = true
    ),
    @CompoundIndex(
            name = "pg_user_status_updated_idx",
            def  = "{'user_id': 1, 'status': 1, 'updated_at': -1, '_id': -1}"
    )
})

public class ProfileGame {
    @Id
    private ObjectId id;

    @Indexed
    @Field(value = "game_id")
    @NotBlank
    private ObjectId gameId;

    @Indexed
    @Field(value = "user_id")
    @NotBlank
    private ObjectId userId;

    @Indexed
    private String username;

    @Indexed
    @Field(value = "source_platform")
    private PlatformType sourcePlatform;
    @Indexed
    @Field(value = "played_on")
    private GamePlatform playedOn;
    private Integer playtime;
    private ProfileGameStatus status;

    @Indexed
    private Integer rating;

    private Boolean liked;
    private Boolean favorite;
    @Field(value = "favorite_order")
    private Integer favoriteOrder;
    private Boolean owned;
    private Boolean playing;
    private Boolean wishlist;
    private Boolean backlog;
    private Boolean mastered;
    @Field(value = "has_review")
    private Boolean hasReview;

    @Field(value = "favorite_screenshots")
    private List<String> favoriteScreenshots;

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
    @Field(value = "game_name")
    private String gameName;
    @Field(value = "game_slug")
    private String gameSlug;
    @Field(value = "game_cover")
    private String gameCover;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getGameId() {
        return gameId;
    }

    public void setGameId(ObjectId gameId) {
        this.gameId = gameId;
    }

    public ObjectId getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }

    public Integer getPlaytime() {
        return playtime;
    }

    public void setPlaytime(Integer playtime) {
        this.playtime = playtime;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public ProfileGameStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileGameStatus status) {
        this.status = status;
    }



    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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
    public Boolean getPlaying() {
        return playing;
    }
    public void setPlaying(Boolean playing) {
        this.playing = playing;
    }
    public Boolean getBacklog() {
        return backlog;
    }
    public void setBacklog(Boolean backlog) {
        this.backlog = backlog;
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
    public  Boolean getHasReview() {
        return hasReview;
    }
    public void setHasReview(Boolean hasReview) {
        this.hasReview = hasReview;
    }

    public List<String> getFavoriteScreenshots() {
        return favoriteScreenshots;
    }

    public void setFavoriteScreenshots(List<String> favoriteScreenshots) {
        this.favoriteScreenshots = favoriteScreenshots;
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

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Integer getFavoriteOrder() {
        return favoriteOrder;
    }

    public void setFavoriteOrder(Integer favoriteOrder) {
        this.favoriteOrder = favoriteOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileGame that = (ProfileGame) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(gameId, that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, gameId);
    }


}
