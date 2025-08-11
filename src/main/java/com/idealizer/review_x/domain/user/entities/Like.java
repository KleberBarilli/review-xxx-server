package com.idealizer.review_x.domain.user.entities;

import com.idealizer.review_x.domain.profile.game.entities.LikeType;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "likeS")
@CompoundIndexes({
        @CompoundIndex(name = "unique_user_review_like", def = "{'user_id': 1, 'review_id': 1}", unique = true, sparse = true),
        @CompoundIndex(name = "unique_user_comment_like", def = "{'user_id': 1, 'comment_id': 1}", unique = true, sparse = true)
})
public class Like {
    @Id
    private ObjectId id;

    @Field(value = "user_id")
    private ObjectId userId;
    @Field(value = "review_id")
    private ObjectId reviewId;
    @Field(value = "comment_id")
    private ObjectId commentId;

    private LikeType type;

    public Like(ObjectId userId, ObjectId reviewId, ObjectId commentId, LikeType type) {
        this.userId = userId;
        this.reviewId = reviewId;
        this.commentId = commentId;
        this.type = type;
    }

    @Field(value = "created_at")
    @CreatedDate
    private Instant createdAt;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getReviewId() {
        return reviewId;
    }

    public void setReviewId(ObjectId reviewId) {
        this.reviewId = reviewId;
    }

    public ObjectId getCommentId() {
        return commentId;
    }

    public void setCommentId(ObjectId commentId) {
        this.commentId = commentId;
    }
    public LikeType getType() {
        return type;
    }
    public void setType(LikeType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
