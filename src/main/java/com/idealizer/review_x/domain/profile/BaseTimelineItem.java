package com.idealizer.review_x.domain.profile;

import com.idealizer.review_x.domain.profile.game.entities.ProfileGameStatus;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public interface BaseTimelineItem {
    @Value("#{target.id != null ? target.id.toHexString() : null}")
    String getId();

    String getTargetName();

    String getTargetSlug();

    String getTargetCover();

    Integer getRating();

    Boolean getLiked();

    ProfileGameStatus getStatus();

    String getEventType();

    Instant getEventAt();

    String getTitle();
}