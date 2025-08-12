package com.idealizer.review_x.infra.libs.twitch.igdb;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.util.Json;


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
        @JsonProperty("game_status")
        Integer gameStatus,
        Integer category,
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
        Long updatedAt,
        @JsonProperty("involved_companies")
        List<IgdbInvolvedCompanyDTO> involvedCompanies,
        List<IgdbWebsiteDTO> websites,
        List<IgdbVideoDTO>videos,
        @JsonProperty("game_engines")
        List<IgdbGameEngine> gameEngines
) {}

record IgdbImageDTO(
        Long id,
        @JsonProperty("image_id")
        String imageId
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record IgdbCompanyDTO(Long id, String name) {}
@JsonIgnoreProperties(ignoreUnknown = true)
record IgdbInvolvedCompanyDTO(Boolean developer, IgdbCompanyDTO company) {}
@JsonIgnoreProperties(ignoreUnknown = true)
record IgdbWebsiteDTO(String id, String url, Integer type) {}
@JsonIgnoreProperties(ignoreUnknown = true)
record IgdbVideoDTO(String id, String name, String video_id) {}
@JsonIgnoreProperties(ignoreUnknown = true)
record IgdbGameEngine(Integer id, String name){}
