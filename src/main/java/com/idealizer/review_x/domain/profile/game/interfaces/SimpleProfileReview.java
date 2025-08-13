package com.idealizer.review_x.domain.profile.game.interfaces;

import com.idealizer.review_x.domain.profile.game.entities.ProfileGameStatus;
import org.bson.types.ObjectId;

import java.time.Instant;

public interface SimpleProfileReview {
    String getId();
    String getGameId();
    String getProfileGameId();
    String getGameName();
    String getGameSlug();
    String getGameCover();
    Boolean getLiked();
    Boolean getReplay();
    Integer getRating();
    ProfileGameStatus getStatus();
    String getContent();
    Boolean getSpoiler();
    Integer getLikeCount();
    Instant getCreatedAt();
}
