package com.idealizer.review_x.infra.libs.twitch.igdb;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public record IgdbGameDTO(
        Long id,
        String name,
        String slug,
        String summary,
        String storyline,
        @JsonProperty("first_release_date")
        Long firstReleaseDate,
        @JsonProperty("total_rating")
        Double totalRating,
        @JsonProperty("total_rating_count")
        Integer totalRatingCount,
        List<String> genres,
        List<String> platforms,
        @JsonProperty("game_modes")
        List<String> gameModes,
        List<Long> expansions,
        @JsonProperty("similar_games")
        List<Long>similarGames,
        IgdbImageDTO cover,
        List<IgdbImageDTO> screenshots,
        @JsonProperty("updated_at")
        Long updatedAt
) {}

record IgdbImageDTO(
        Long id,
        @JsonProperty("image_id")
        String imageId
) {}
