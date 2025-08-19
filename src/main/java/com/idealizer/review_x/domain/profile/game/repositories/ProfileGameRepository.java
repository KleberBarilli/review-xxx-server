package com.idealizer.review_x.domain.profile.game.repositories;

import com.idealizer.review_x.domain.profile.game.entities.ProfileGame;
import com.idealizer.review_x.domain.profile.game.interfaces.BaseProfileGame;
import com.idealizer.review_x.domain.profile.game.interfaces.SimpleProfileGame;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileGameRepository extends MongoRepository<ProfileGame, ObjectId>, ProfileGameRepositoryCustom {


    Optional<ProfileGame> findByGameId(ObjectId gameId);

    List<SimpleProfileGame> findProjectedByUserIdAndFavoriteIsTrue(ObjectId userId);

    Optional<SimpleProfileGame> findProjectedByUsernameAndGameSlug(String username, String gameSlug);

    List<BaseProfileGame> findProjectedByUsername(String username);

    List<ProfileGame> findByUserIdAndFavoriteIsTrue(ObjectId userId);

    Optional<ProfileGame> findByUserIdAndGameId(ObjectId userId, ObjectId gameId);

    List<ProfileGame> findByUserIdAndGameIdIn(ObjectId userId, List<ObjectId> gameIds);

    List<SimpleProfileGame> findByIdIn(List<ObjectId> ids);
}
