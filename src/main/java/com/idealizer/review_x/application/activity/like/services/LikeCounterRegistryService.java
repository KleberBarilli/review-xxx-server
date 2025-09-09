package com.idealizer.review_x.application.activity.like.services;

import com.idealizer.review_x.application.activity.like.ports.LikeCounterPort;
import com.idealizer.review_x.domain.core.activity.like.entities.LikeType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LikeCounterRegistryService {
    private final Map<LikeType, LikeCounterPort> byType;

    public LikeCounterRegistryService(List<LikeCounterPort> counters) {
        this.byType = counters.stream()
                .collect(Collectors.toMap(LikeCounterPort::supports, c -> c));
    }

    public LikeCounterPort get(LikeType type) {
        var c = byType.get(type);
        if (c == null) throw new IllegalArgumentException("Unsupported LikeType: " + type);
        return c;
    }
}