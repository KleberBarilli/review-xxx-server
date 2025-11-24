package com.idealizer.review_x.domain.core.profile;

import com.idealizer.review_x.domain.core.profile.game.entities.PlatformType;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGameStatus;
import com.idealizer.review_x.domain.game.entities.enums.GamePlatform;
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

    Boolean getHasReview();

    Boolean getMastered();

    GamePlatform getPlayedOn();

    PlatformType getSourcePlatform();

    ProfileGameStatus getStatus();

    String getEventType();

    Instant getEventAt();

    String getTitle();
}