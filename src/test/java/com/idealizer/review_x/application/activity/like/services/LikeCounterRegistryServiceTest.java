package com.idealizer.review_x.application.activity.like.services;

import com.idealizer.review_x.application.activity.like.ports.LikeCounterPort;
import com.idealizer.review_x.domain.core.activity.like.entities.LikeType;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LikeCounterRegistryServiceTest {

    static class ReviewCounterStub implements LikeCounterPort {
        @Override public LikeType supports() { return LikeType.REVIEW; }
        @Override public void inc(ObjectId targetId, int delta) { /* no-op */ }
    }
    static class CommentCounterStub implements LikeCounterPort {
        @Override public LikeType supports() { return LikeType.COMMENT; }
        @Override public void inc(ObjectId targetId, int delta) { /* no-op */ }
    }

    @Test
    void shouldResolveCounterByType() {
        var registry = new LikeCounterRegistryService(List.of(
                new ReviewCounterStub(), new CommentCounterStub()
        ));

        assertTrue(registry.get(LikeType.REVIEW) instanceof ReviewCounterStub);
        assertTrue(registry.get(LikeType.COMMENT) instanceof CommentCounterStub);
    }

    @Test
    void shouldThrowWhenTypeUnsupported() {
        var registry = new LikeCounterRegistryService(List.of(new ReviewCounterStub()));
        var ex = assertThrows(IllegalArgumentException.class, () -> registry.get(LikeType.ACTIVITY));
        assertTrue(ex.getMessage().contains("Unsupported LikeType"));
    }
}