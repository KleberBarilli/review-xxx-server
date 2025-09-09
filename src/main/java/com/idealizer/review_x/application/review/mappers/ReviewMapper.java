package com.idealizer.review_x.application.review.mappers;

import com.idealizer.review_x.application.games.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.application.review.commands.CreateUpdateReviewCommand;
import com.idealizer.review_x.domain.core.review.entities.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewMapper {
    Review toEntity(CreateUpdateReviewCommand command);

    @Mapping(target = "title", source = "reviewTitle")
    @Mapping(target = "content", source = "reviewContent")
    @Mapping(target = "spoiler", source = "reviewSpoiler")
    @Mapping(target = "replay", source = "reviewReplay")
    @Mapping(target = "startedAt", source = "reviewStartedAt")
    @Mapping(target = "finishedAt", source = "reviewFinishedAt")
    CreateUpdateReviewCommand toCommand(UpsertProfileGameReviewCommand source);

    @Mapping(source = "reviewTitle", target = "title")
    @Mapping(source = "reviewContent", target = "content")
    @Mapping(source = "reviewSpoiler", target = "spoiler")
    @Mapping(source = "reviewReplay", target = "replay")
    @Mapping(source = "reviewStartedAt", target = "startedAt")
    @Mapping(source = "reviewFinishedAt", target = "finishedAt")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReviewFromCommand(UpsertProfileGameReviewCommand source, @MappingTarget Review target);
}
