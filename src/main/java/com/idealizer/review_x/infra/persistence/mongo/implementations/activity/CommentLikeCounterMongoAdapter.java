package com.idealizer.review_x.infra.persistence.mongo.implementations.activity;

import com.idealizer.review_x.application.activity.like.ports.LikeCounterPort;
import com.idealizer.review_x.domain.activity.comment.entities.Comment;
import com.idealizer.review_x.domain.activity.like.entities.LikeType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class CommentLikeCounterMongoAdapter implements LikeCounterPort {

    private final MongoTemplate mongo;

    public CommentLikeCounterMongoAdapter(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public LikeType supports() {
        return LikeType.COMMENT;
    }

    @Override
    public void inc(ObjectId targetId, int delta) {
        Query q = Query.query(Criteria.where("_id").is(targetId));
        Update u = new Update().inc("likeCount", delta);
        mongo.updateFirst(q, u, Comment.class);
    }

}
