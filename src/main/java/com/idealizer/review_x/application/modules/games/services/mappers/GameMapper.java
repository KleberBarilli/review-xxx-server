package com.idealizer.review_x.application.modules.games.services.mappers;

import com.idealizer.review_x.application.modules.games.services.outputs.GameResponseDTO;
import com.idealizer.review_x.application.modules.games.services.outputs.SimpleGameResponseDTO;
import com.idealizer.review_x.application.modules.games.entities.Game;
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
    @Mapping(target = "totalRating", source = "rating")
    @Mapping(target = "totalRatingCount", source = "ratingCount")
    Game toEntity(GameResponseDTO dto);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "externalId", source = "igdbId")
    @Mapping(target = "dlcsIds", source = "dlcsIgdbIds")
    @Mapping(target = "similarGamesIds", source = "similarGamesIgdbIds")
    @Mapping(target = "rating", source = "totalRating")
    @Mapping(target = "ratingCount", source = "totalRatingCount")
    GameResponseDTO toDetailedDomain(Game game);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "externalId", source = "igdbId")
    @Mapping(target = "rating", source = "totalRating")
    @Mapping(target = "ratingCount", source = "totalRatingCount")
    SimpleGameResponseDTO toSimpleDomain(Game game);
    List<SimpleGameResponseDTO> toSimpleDomainList(List<Game> games);

    default ObjectId map(String value) {
        return value != null ? new ObjectId(value) : null;
    }

    default String map(ObjectId value) {
        return value != null ? value.toHexString() : null;
    }
}
