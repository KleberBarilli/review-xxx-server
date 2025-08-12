package com.idealizer.review_x.application.games.game.mappers;

import com.idealizer.review_x.application.games.game.responses.GameResponse;
import com.idealizer.review_x.application.games.game.responses.SimpleGameResponse;
import com.idealizer.review_x.domain.game.entities.Game;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "igdbId", source = "externalId")
    @Mapping(target = "dlcsIgdbIds", source = "dlcsIds")
    @Mapping(target = "similarGamesIgdbIds", source = "similarGamesIds")
    Game toEntity(GameResponse dto);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "externalId", source = "igdbId")
    @Mapping(target = "dlcsIds", source = "dlcsIgdbIds")
    @Mapping(target = "similarGamesIds", source = "similarGamesIgdbIds")
    GameResponse toDetailedDomain(Game game);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "externalId", source = "igdbId")
    SimpleGameResponse toSimpleDomain(Game game);
    List<SimpleGameResponse> toSimpleDomainList(List<Game> games);

    default ObjectId map(String value) {
        return value != null ? new ObjectId(value) : null;
    }

    default String map(ObjectId value) {
        return value != null ? value.toHexString() : null;
    }
}
