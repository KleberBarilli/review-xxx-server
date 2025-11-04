package com.idealizer.review_x.application.games.game.responses;

import com.idealizer.review_x.domain.game.entities.enums.*;
import com.idealizer.review_x.domain.game.entities.records.AlternativeName;
import com.idealizer.review_x.domain.game.entities.records.GameTimeToBeat;
import com.idealizer.review_x.domain.game.entities.records.GameWebsite;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class GameResponse {
    private String id;
    private Long externalId;
    private List<Long> dlcsIds;
    private String name;
    private List<AlternativeName> alternativeNames;
    private String slug;
    private String storyline;
    private List<String> screenshots;
    private GameType type;
    private GameStatus status;

    private LocalDate firstReleaseDate;
    private List<GameGenre> genres;
    private List<GameMode> modes;
    private List<GamePlatform> platforms;
    private String developer;
    private String trailerUrl;
    private GameTimeToBeat timeToBeat;
    private String cover;
    private Double totalRating;
    private Integer totalRatingCount;
    private Instant updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public List<Long> getDlcsIds() {
        return dlcsIds;
    }

    public void setDlcsIds(List<Long> dlcsIds) {
        this.dlcsIds = dlcsIds;
    }

    public String getName() {
        return name;
    }

    public String getStoryline() {
        return storyline;
    }

    public void setStoryline(String storyline) {
        this.storyline = storyline;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AlternativeName> getAlternativeNames() {
        return alternativeNames;
    }

    public void setAlternativeNames(List<AlternativeName> alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public LocalDate getFirstReleaseDate() {
        return firstReleaseDate;
    }

    public void setFirstReleaseDate(LocalDate firstReleaseDate) {
        this.firstReleaseDate = firstReleaseDate;
    }

    public List<GameGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<GameGenre> genres) {
        this.genres = genres;
    }

    public List<GameMode> getModes() {
        return modes;
    }

    public void setModes(List<GameMode> modes) {
        this.modes = modes;
    }

    public List<GamePlatform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<GamePlatform> platforms) {
        this.platforms = platforms;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public GameTimeToBeat getTimeToBeat() {
        return timeToBeat;
    }

    public void setTimeToBeat(GameTimeToBeat timeToBeat) {
        this.timeToBeat = timeToBeat;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
