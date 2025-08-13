package com.idealizer.review_x.infra.http.controllers.game.profile.mappers;

import com.idealizer.review_x.application.games.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.infra.http.controllers.game.profile.dto.UpsertProfileGameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileGameDTOMapper {
    UpsertProfileGameReviewCommand toCommand(UpsertProfileGameDTO dto);
}