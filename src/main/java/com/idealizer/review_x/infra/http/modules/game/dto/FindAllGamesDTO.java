package com.idealizer.review_x.infra.http.modules.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
@Schema(name = "FindAllGames")
public record FindAllGamesDTO(

        @Schema(description = "Max 50", example = "10")
        @Min(1)
        @Max(50)
        int limit,

        @Schema(example = "0")
        @Min(0)
        int pageNumber,

        @Schema(description = "Field to order", example = "updatedAt")
        String sort,

        @Schema(description = "Order, asc/desc", example = "asc")
        String order
) {
}
