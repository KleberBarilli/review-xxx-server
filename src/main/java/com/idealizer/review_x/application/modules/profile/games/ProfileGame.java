package com.idealizer.review_x.application.modules.profile.games;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Document(collection = "profile_games")
public class ProfileGame {
    @Id
    private String id;

    @Indexed
    @Field(value = "user_id")
    private String userId;

    @Indexed
    private PlatformType platform;

    @Indexed
    @Field(value = "external_id")
    private String externalId;

    @Field(value = "original_name")
    private String originalName;

    @Indexed
    private String slug;

    @Field(value = "matched_game_id")
    private String matchedGameId; //(IGDB)

    @Field(value = "playtime_in_minutes")
    private Integer playtimeInMinutes;

    private Boolean favorite;
    private Boolean owned;
    private Boolean completed;
    private Boolean wishlist;

    @Field(value = "created_at")
    private Instant createdAt;
    @Field(value = "updated_at")
    private Instant updatedAt;

    @Field(value = "additional_data")
    private Map<String, Object> additionalData;
}
