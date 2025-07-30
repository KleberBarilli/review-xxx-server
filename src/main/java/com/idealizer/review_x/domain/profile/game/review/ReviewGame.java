package com.idealizer.review_x.domain.profile.game.review;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "review_games")
public class ReviewGame {
    @Id
    private ObjectId id;

    @Field(value = "user_id")
    private ObjectId userId;
    @Field(value = "profile_game_id")
    private ObjectId profileGameId;
    @Field(value = "game_id")
    private ObjectId gameId;


    private String title;
    private String content;
    private Boolean spoiler;
    private Boolean replay;
    private Integer rating;

    private Integer likes;
    private Integer comments;

    @Field(value = "started_at")
    private Instant startedAt;
    @Field(value = "finished_at")
    private Instant finishedAt;
    @Field(value = "created_at")
    @CreatedDate
    private Instant createdAt;
    @Field(value = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;


}
