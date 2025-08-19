package com.idealizer.review_x.infra.persistence.mongo.activity.like.adapters;


import com.idealizer.review_x.domain.activity.like.entities.LikeType;
import com.idealizer.review_x.domain.profile.game.entities.ReviewGame;
import com.idealizer.review_x.infra.persistence.mongo.activity.ReviewLikeCounterMongoAdapter;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewLikeCounterMongoAdapterTest {

    @Test
    void shouldCallUpdateFirstWithInc() {
        var mongo = mock(MongoTemplate.class);
        var adapter = new ReviewLikeCounterMongoAdapter(mongo);

        var id = new ObjectId();
        adapter.inc(id, +1);

        ArgumentCaptor<Query> qCap = ArgumentCaptor.forClass(Query.class);
        ArgumentCaptor<Update> uCap = ArgumentCaptor.forClass(Update.class);

        verify(mongo, times(1))
                .updateFirst(qCap.capture(), uCap.capture(), eq(ReviewGame.class));

        assertEquals(id, qCap.getValue().getQueryObject().get("_id"));
        assertTrue(uCap.getValue().getUpdateObject().containsKey("$inc"));

        assertEquals(LikeType.REVIEW, adapter.supports());
    }
}