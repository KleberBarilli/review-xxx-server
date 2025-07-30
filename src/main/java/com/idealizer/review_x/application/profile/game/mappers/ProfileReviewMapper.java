package com.idealizer.review_x.application.profile.game.mappers;

import com.idealizer.review_x.application.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.domain.profile.game.review.ReviewGame;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileReviewMapper {

    @Mapping( target = "title", source = "reviewTitle")
    @Mapping(target = "content", source = "reviewContent")
    @Mapping(target = "spoiler", source = "reviewSpoiler")
    @Mapping(target = "replay", source = "reviewReplay")
    @Mapping(target = "startedAt", source = "reviewStartedAt")
    @Mapping(target = "finishedAt", source = "reviewFinishedAt")
    ReviewGame toEntity(UpsertProfileGameReviewCommand command);
}
