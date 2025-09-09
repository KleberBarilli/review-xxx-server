package com.idealizer.review_x.infra.persistence.mongo.implementations.activity;

import com.idealizer.review_x.application.activity.like.ports.LikeCounterPort;
import com.idealizer.review_x.domain.core.activity.comment.entities.Comment;
import com.idealizer.review_x.domain.core.activity.like.entities.LikeType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class CommentLikeCounterMongoAdapter implements LikeCounterPort {

    private final MongoTemplate coreMongo;

    public CommentLikeCounterMongoAdapter(@Qualifier("coreMongoTemplate") MongoTemplate coreMongo) {
        this.coreMongo = coreMongo;
    }

    @Override
    public LikeType supports() {
        return LikeType.COMMENT;
    }

    @Override
    public void inc(ObjectId targetId, int delta) {
        Query q = Query.query(Criteria.where("_id").is(targetId));
        Update u = new Update().inc("likeCount", delta);
        coreMongo.updateFirst(q, u, Comment.class);
    }

}
