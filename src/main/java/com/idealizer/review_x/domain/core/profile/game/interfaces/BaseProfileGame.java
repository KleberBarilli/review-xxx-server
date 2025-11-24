package com.idealizer.review_x.domain.core.profile.game.interfaces;

import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public interface BaseProfileGame {

    @Value("#{target.id != null ? target.id.toHexString() : null}")
    String getId();
    String getGameName();
    String getGameSlug();
    String getGameCover();
    Boolean getLiked();
    Boolean getHasReview();
    Boolean getBacklog();
    Boolean getPlaying();
    Boolean getMastered();
    Boolean getOwned();
    Boolean getWishlist();
    Integer getRating();
    ProfileGameStatus getStatus();
    Instant getCreatedAt();
    Instant getUpdatedAt();

}
