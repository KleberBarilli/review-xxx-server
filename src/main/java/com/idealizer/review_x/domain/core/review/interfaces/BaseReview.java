package com.idealizer.review_x.domain.core.review.interfaces;

import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public interface BaseReview {
    @Value("#{target.id != null ? target.id.toHexString() : null}")
    String getId();
    String getTargetName();
    String getTargetSlug();
    String getTargetCover();
    Boolean getLiked();
    String getContent();
    Boolean getSpoiler();
    Boolean getReplay();
    Integer getRating();
    ProfileGameStatus getStatus();
    Instant getCreatedAt();
    Instant getUpdatedAt();
}
