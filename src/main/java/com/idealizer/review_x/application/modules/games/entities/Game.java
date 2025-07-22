package com.idealizer.review_x.application.modules.games.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Document(collection = "games")
public class Game {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @Field(value = "igdb_id")
    private Long igdbId;
    @Field(value = "parent_igdb_id")
    private Long parentIgdbId;
    @Field(value = "dlcs_igdb_ids")
    private List<Long> dlcsIgdbIds;
    @Field(value = "similar_games_igdb_ids")
    private List<Long> similarGamesIgdbIds;
    private String name;
    @Indexed
    private String slug;
    private String summary;
    private String storyline;
    @Field(value = "first_release_date")
    private LocalDate firstReleaseDate;
    @Field(value = "total_rating")
    private Double totalRating;
    @Field(value = "total_rating_count")
    private Integer totalRatingCount;

    private List<GameGenre> genres;
    private List<GamePlatform> platforms;
    private List<GameMode> modes;
    private String cover;
    private List<String> screenshots;
    @Field(value = "created_at")
    private Instant createdAt;
    @Field(value = "updated_at")
    private Instant updatedAt;

    public Game() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getIgdbId() {
        return igdbId;
    }

    public void setIgdbId(Long igdbId) {
        this.igdbId = igdbId;
    }

    public Long getParentIgdbId() {
        return parentIgdbId;
    }

    public void setParentIgdbId(Long parentIgdbId) {
        this.parentIgdbId = parentIgdbId;
    }

    public List<Long> getDlcsIgdbIds() {
        return dlcsIgdbIds;
    }
    public void setDlcsIgdbIds(List<Long> dlcsIgdbIds) {
        this.dlcsIgdbIds = dlcsIgdbIds;
    }

    public List<Long> getSimilarGamesIgdbIds() {
        return similarGamesIgdbIds;
    }
    public void setSimilarGamesIgdbIds(List<Long> similarGamesIgdbIds) {
        this.similarGamesIgdbIds = similarGamesIgdbIds;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStoryline() {
        return storyline;
    }

    public void setStoryline(String storyline) {
        this.storyline = storyline;
    }

    public LocalDate getFirstReleaseDate() {
        return firstReleaseDate;
    }

    public void setFirstReleaseDate(LocalDate firstReleaseDate) {
        this.firstReleaseDate = firstReleaseDate;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public Integer getTotalRatingCount() {
        return totalRatingCount;
    }

    public void setTotalRatingCount(Integer totalRatingCount) {
        this.totalRatingCount = totalRatingCount;
    }

    public List<GameGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<GameGenre> genres) {
        this.genres = genres;
    }

    public List<GamePlatform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<GamePlatform> platforms) {
        this.platforms = platforms;
    }

    public List<GameMode> getModes() {
        return modes;
    }
    public void setModes(List<GameMode> modes) {
        this.modes = modes;
    }


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
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


}
