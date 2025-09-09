package com.idealizer.review_x.application.activity.like.ports;

import com.idealizer.review_x.domain.core.activity.like.entities.LikeType;
import org.bson.types.ObjectId;

public interface LikeCounterPort {
    LikeType supports();
    void inc(ObjectId targetId, int delta);
}
