package com.idealizer.review_x.domain.game.entities;

import com.idealizer.review_x.common.dtos.game.DiscoverPreset;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document("game_rankings")
@CompoundIndexes({
        @CompoundIndex(name = "preset_rank_idx", def = "{'preset': 1, 'rank': 1}"),
        @CompoundIndex(name = "preset_igdb_unique_idx", def = "{'preset': 1, 'igdbId': 1}", unique = true)
})
public class GameRanking {
    @Id
    private ObjectId id;

    private DiscoverPreset preset;

    private Long igdbId;
    private Integer rank;
    private Double score;

    private Signals signals;
    private GameSnapshot game;

    private Instant generatedAt;
    private Instant sourceSyncAt;
    private Instant expiresAt;

    public static class Signals {
        public Double totalRating;
        public Integer totalRatingCount;
        public Double popularity;
        public Integer follows;
        public Integer hypes;
        public Instant firstReleaseDate;
        public Instant updatedAt;
    }

    public static class GameSnapshot {
        public String name;
        public String slug;
        public String cover;
        public Instant firstReleaseDate;
        public List<Long> genres;
        public List<Long> platforms;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public DiscoverPreset getPreset() {
        return preset;
    }

    public void setPreset(DiscoverPreset preset) {
        this.preset = preset;
    }

    public Long getIgdbId() {
        return igdbId;
    }

    public void setIgdbId(Long igdbId) {
        this.igdbId = igdbId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Signals getSignals() {
        return signals;
    }

    public void setSignals(Signals signals) {
        this.signals = signals;
    }

    public GameSnapshot getGame() {
        return game;
    }

    public void setGame(GameSnapshot game) {
        this.game = game;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Instant getSourceSyncAt() {
        return sourceSyncAt;
    }

    public void setSourceSyncAt(Instant sourceSyncAt) {
        this.sourceSyncAt = sourceSyncAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
