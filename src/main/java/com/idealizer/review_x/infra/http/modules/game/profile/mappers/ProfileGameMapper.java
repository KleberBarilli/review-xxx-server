package com.idealizer.review_x.infra.http.modules.game.profile.mappers;

import com.idealizer.review_x.application.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.infra.http.modules.game.profile.dto.UpsertProfileGameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileGameMapper {

    ProfileGameMapper INSTANCE = Mappers.getMapper(ProfileGameMapper.class);

    @Mapping(target = "reviewTitle", source = "review.title")
    @Mapping(target = "reviewContent", source = "review.content")
    @Mapping(target = "reviewSpoiler", source = "review.spoiler")
    @Mapping(target = "reviewReplay", source = "review.replay")
    @Mapping(target = "reviewReplayStartedAt", source = "review.startedAt")
    @Mapping(target = "reviewFinishedAt", source = "review.finishedAt")

    UpsertProfileGameReviewCommand toCommand(UpsertProfileGameDTO dto);
}