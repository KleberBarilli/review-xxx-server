package com.idealizer.review_x.domain.game.entities.records;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

public record GameTimeToBeat(Integer hastily, Integer normally, Integer completely, Integer count,
                             @Field("updated_at") Instant updatedAt) {
}
