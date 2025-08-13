package com.idealizer.review_x.domain.game.entities.records;

import com.idealizer.review_x.domain.game.entities.enums.GameWebsiteType;

public record GameWebsite(GameWebsiteType type, String url) {
}
