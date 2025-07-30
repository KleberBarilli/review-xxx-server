package com.idealizer.review_x.domain.profile.games;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

@Document(collection = "profile_games")
@CompoundIndex(name = "unique_slug_per_user", def = "{'user_id': 1, 'slug': 1}", unique = true)
@CompoundIndex(name = "user_game_idx", def = "{'user_id': 1, 'matched_game_id': 1}", unique = true)

public class ProfileGame {
    @Id
    private String id;

    @Indexed
    @Field(value = "matched_game_id")
    private String matchedGameId; //(IGDB)

    @Indexed
    @Field(value = "user_id")
    private String userId;

    @Indexed
    private PlatformType platform;

    @Field(value = "original_name")
    private String originalName;

    private String slug;

    @Indexed
    @Field(value = "playtime_in_minutes")
    private Integer playtimeInMinutes;

    private Boolean playing;
    private Boolean favorite;
    private Boolean owned;
    private Boolean wishlist;
    private Boolean mastered;

    @Field(value = "created_at")
    @CreatedDate
    private Instant createdAt;
    @Field(value = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    public Integer getPlaytimeInMinutes() {
        return playtimeInMinutes;
    }

    public void setPlaytimeInMinutes(Integer playtimeInMinutes) {
        this.playtimeInMinutes = playtimeInMinutes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchedGameId() {
        return matchedGameId;
    }

    public void setMatchedGameId(String matchedGameId) {
        this.matchedGameId = matchedGameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PlatformType getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformType platform) {
        this.platform = platform;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileGame that = (ProfileGame) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(matchedGameId, that.matchedGameId) &&
                platform == that.platform &&
                Objects.equals(slug, that.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, matchedGameId, platform, slug);
    }


}
