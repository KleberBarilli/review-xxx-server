package com.idealizer.review_x.infra.http.modules.game.profile.mappers;

import com.idealizer.review_x.application.games.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.infra.http.modules.game.profile.dto.UpsertProfileGameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileGameDTOMapper {
    UpsertProfileGameReviewCommand toCommand(UpsertProfileGameDTO dto);
}