package com.idealizer.review_x.domain.profile.game.review;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "review_comments")
public class ReviewComment {
    @Id
    private ObjectId id;
    @Field(value = "user_id")
    private ObjectId userId;
    @Field(value = "review_id")
    private ObjectId reviewId;

    private Integer likes;

}
