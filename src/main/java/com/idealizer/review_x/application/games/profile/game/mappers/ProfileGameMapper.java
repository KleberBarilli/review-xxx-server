package com.idealizer.review_x.application.games.profile.game.mappers;

import com.idealizer.review_x.application.games.profile.game.commands.CreateUpdateProfileGameCommand;
import com.idealizer.review_x.application.games.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.domain.core.profile.game.entities.ProfileGame;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileGameMapper {
    ProfileGame toEntity(CreateUpdateProfileGameCommand command);

    CreateUpdateProfileGameCommand toCreateUpdateCommand(UpsertProfileGameReviewCommand source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfileGameFromCommand(UpsertProfileGameReviewCommand source, @MappingTarget ProfileGame target);

}
