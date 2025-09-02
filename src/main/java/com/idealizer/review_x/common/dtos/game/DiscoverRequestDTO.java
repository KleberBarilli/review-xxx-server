package com.idealizer.review_x.common.dtos.game;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@ParameterObject
@Schema(name = "FindGamesByPreset")
public record DiscoverRequestDTO(
        @Schema(description = "Filter", example = "TOP_RATED")
        DiscoverPreset preset,
        @Schema()
        List<Long> platformIds,
        @Schema()
        List<Long> genreIds
) {
}