package com.idealizer.review_x.domain.profile.game.interfaces;

import com.idealizer.review_x.domain.profile.game.ProfileGameStatus;

public interface SimpleProfileGame {

    String getGameId();
    String getGameName();
    String getGameSlug();
    String getGameCover();
    Boolean getLiked();
    Boolean getHasReview();
    Integer getRating();
    ProfileGameStatus getStatus();
    Integer getFavoriteOrder();
}