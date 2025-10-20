package com.idealizer.review_x.domain.core.profile.game.interfaces;

import com.idealizer.review_x.domain.core.profile.game.entities.PlatformType;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;
import com.idealizer.review_x.domain.game.entities.enums.GamePlatform;

import java.time.Instant;

public interface SimpleProfileGame {

    String getId();
    String getUserId();
    String getGameId();
    String getGameName();
    String getGameSlug();
    String getGameCover();
    Boolean getLiked();
    Boolean getMastered();
    Boolean getBacklog();
    Boolean getPlaying();
    Boolean getWishlist();
    GamePlatform getPlayedOn();
    PlatformType getSourcePlatform();
    Boolean getHasReview();
    Instant getStartedAt();
    Instant getFinishedAt();
    Integer getRating();
    ProfileGameStatus getStatus();
    Integer getFavoriteOrder();
    Instant getCreatedAt();
}