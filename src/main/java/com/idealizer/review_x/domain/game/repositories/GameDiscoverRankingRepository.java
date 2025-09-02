package com.idealizer.review_x.domain.game.repositories;

import com.idealizer.review_x.common.dtos.game.DiscoverPreset;
import com.idealizer.review_x.domain.game.entities.GameRanking;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameDiscoverRankingRepository extends MongoRepository<GameRanking, ObjectId> {

    List<GameRanking> findAllByPreset(DiscoverPreset preset);
}
