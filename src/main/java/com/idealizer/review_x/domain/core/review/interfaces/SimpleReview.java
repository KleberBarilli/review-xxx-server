package com.idealizer.review_x.domain.core.review.interfaces;

import org.bson.types.ObjectId;
import java.time.Instant;

public interface SimpleReview {
    ObjectId getId();
    ObjectId getUserId();
    String getUsername();

    ObjectId getProfileTargetId();

    String getTitle();
    String getContent();
    Boolean getSpoiler();
    Boolean getReplay();
    long getLikeCount();

    String getTargetName();
    String getTargetSlug();
    String getTargetCover();

    Instant getCreatedAt();
    Instant getUpdatedAt();
}
