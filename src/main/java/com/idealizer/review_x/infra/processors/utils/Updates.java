package com.idealizer.review_x.infra.processors.utils;

import org.springframework.data.mongodb.core.query.Update;

public final class Updates {
    private Updates() {}

    public static Update setIfNotNull(Update u, String field, Object value) {
        return value == null ? u : u.set(field, value);
    }
}
