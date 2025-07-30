package com.idealizer.review_x.application.profile.game.mappers;

import com.idealizer.review_x.application.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.domain.profile.game.ProfileGame;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileGameMapper {
    ProfileGame toEntity(UpsertProfileGameReviewCommand command);
}
