package com.idealizer.review_x.domain.game.entities;

import com.idealizer.review_x.domain.game.entities.enums.*;
import com.idealizer.review_x.domain.game.entities.records.AlternativeName;
import com.idealizer.review_x.domain.game.entities.records.GameTimeToBeat;
import com.idealizer.review_x.domain.game.entities.records.GameWebsite;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

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
    private String name;
    @Indexed(unique = true)
    private String slug;
    @Field(value = "alternative_names")
    private List<AlternativeName> alternativeNames;
    @Indexed
    private List<String> aliases;
    @Indexed
    private GameType type;
    @Field(value = "first_release_date")
    private LocalDate firstReleaseDate;
    @Field(value = "total_rating")
    private Double totalRating;
    @Field(value = "total_rating_count")
    private Integer totalRatingCount;

    @Indexed
    private List<GameGenre> genres;
    @Indexed
    private List<GamePlatform> platforms;
    @Indexed
    private List<GameMode> modes;
    @Indexed
    private GameStatus status;
    private String cover;
    @Indexed
    private String developer;
    private String trailerUrl;
    @Field(value = "time_to_beat")
    private GameTimeToBeat timeToBeat;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public List<AlternativeName> getAlternativeNames() {
        return alternativeNames;
    }

    public void setAlternativeNames(List<AlternativeName> alternativeNames) {
        this.alternativeNames = alternativeNames;
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

    public GameTimeToBeat getTimeToBeat() {
        return timeToBeat;
    }

    public void setTimeToBeat(GameTimeToBeat timeToBeat) {
        this.timeToBeat = timeToBeat;
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
