package com.idealizer.review_x.application.games.game.responses;

import com.idealizer.review_x.domain.game.entities.enums.GameGenre;
import com.idealizer.review_x.domain.game.entities.enums.GameMode;
import com.idealizer.review_x.domain.game.entities.enums.GamePlatform;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class GameResponse {
    private String id;
    private Long externalId;
    private List<Long> dlcsIds;
    private List<Long> similarGamesIds;
    private String name;
    private LocalDate firstReleaseDate;
    private List<GameGenre> genres;
    private List<GameMode> modes;
    private List<GamePlatform> platforms;
    private List<String> engines;
    private String developer;
    private String trailerUrl;

    private String storyline;
    private String summary;
    private String cover;
    private List<String> screenshots;
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

    public List<Long> getSimilarGamesIds() {
        return similarGamesIds;
    }

    public void setSimilarGamesIds(List<Long> similarGamesIds) {
        this.similarGamesIds = similarGamesIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStoryline() {
        return storyline;
    }

    public void setStoryline(String storyline) {
        this.storyline = storyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public Double getTotalRating() {
        return totalRating;
    }

    public void setRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public Integer getTotalRatingCount() {
        return totalRatingCount;
    }

    public void setRatingCount(Integer totalRatingCount) {
        this.totalRatingCount = totalRatingCount;
    }

    public List<String> getEngines() {
        return engines;
    }

    public void setEngines(List<String> engines) {
        this.engines = engines;
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

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
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
