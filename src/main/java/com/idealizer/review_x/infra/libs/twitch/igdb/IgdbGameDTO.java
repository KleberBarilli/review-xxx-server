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
        List<Long> dlcs
) {}
