package com.idealizer.review_x.domain.core.profile.game.interfaces;

import com.idealizer.review_x.domain.core.LogID;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;

import java.time.Instant;

public interface SimpleProfileReview {
    String getId();
    String getTargetId();
    String getProfileTargetId();
    String getTargetName();
    String getTargetSlug();
    String getTargetCover();
    Boolean getLiked();
    LogID getTargetType();
    Boolean getReplay();
    Integer getRating();
    ProfileGameStatus getStatus();
    String getContent();
    Boolean getSpoiler();
    Integer getLikeCount();
    Instant getCreatedAt();
}
