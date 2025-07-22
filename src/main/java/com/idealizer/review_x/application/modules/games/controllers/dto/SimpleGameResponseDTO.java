package com.idealizer.review_x.application.modules.games.controllers.dto;

import com.idealizer.review_x.application.modules.games.entities.GameGenre;
import com.idealizer.review_x.application.modules.games.entities.GameMode;
import com.idealizer.review_x.application.modules.games.entities.GamePlatform;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class SimpleGameResponseDTO {
    private String id;
    private Long externalId;
    private String name;
    private LocalDate firstReleaseDate;
    private List<GameGenre> genres;
    private List<GameMode> modes;
    private List<GamePlatform> platforms;
    private String cover;
    private Double rating;
    private Integer ratingCount;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
