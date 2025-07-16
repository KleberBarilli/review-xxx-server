package com.idealizer.review_x.application.modules.provider.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "providers")
public class Provider {
    @Id
    private ObjectId id;

    private PlatformType platform;
    private String note;
    @Field(value = "access_token")
    private String accessToken;
    @Field(value = "client_id")
    private String clientId;
    @Field(value = "client_secret")
    private String clientSecret;
    @Field(value="expires_at")
    private Instant expiresAt;
    @Field(value = "created_at")
    @CreatedDate
    private Instant createdAt;
    @Field(value = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;
}
