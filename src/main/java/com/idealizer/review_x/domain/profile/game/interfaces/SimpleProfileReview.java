package com.idealizer.review_x.domain.profile.game.interfaces;

import com.idealizer.review_x.domain.profile.game.ProfileGameStatus;

import java.time.Instant;

public interface SimpleProfileReview {
    String getGameId();
    String getProfileGameId();
    String getGameName();
    String getGameSlug();
    String getGameCover();
    Boolean getLiked();
    Integer getRating();
    ProfileGameStatus getStatus();
    String getContent();
    Boolean getSpoiler();
    Integer getLikes();
    Instant getCreatedAt();
}
