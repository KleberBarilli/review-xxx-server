package com.idealizer.review_x.infra.libs.twitch.igdb.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class IgdbPopularityClient {

    private final RestClient igdb;

    public IgdbPopularityClient(@Qualifier("igdbRestClient") RestClient igdb) {
        this.igdb = igdb;
    }

    /** Retorna Top-K para um popularity_type específico do IGDB. */
    public List<PopularityPrimitive> topKByType(int popularityTypeId, int k) {
        String body = """
      fields game_id, value, popularity_type;
      sort value desc;
      limit %d;
      where popularity_type = %d;
      """.formatted(k, popularityTypeId);

        try {
            return igdb.post()
                    .uri("/v4/popularity_primitives")
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientResponseException e) {
            // joga erro com corpo da resposta pra facilitar debug
            throw new RuntimeException(
                    "IGDB error %d: %s".formatted(e.getRawStatusCode(), e.getResponseBodyAsString()), e);
        }
    }

    // DTO alinhado ao snake_case retornado pelo IGDB
    public record PopularityPrimitive(
            @JsonProperty("game_id") Long gameId,
            @JsonProperty("value") Double value,
            @JsonProperty("popularity_type") Integer popularityType
    ) {}
}