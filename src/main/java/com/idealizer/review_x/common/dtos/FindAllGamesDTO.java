package com.idealizer.review_x.common.dtos;

import com.idealizer.review_x.domain.game.entities.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDate;
import java.util.List;

@ParameterObject
@Schema(name = "FindAllGames")
public record FindAllGamesDTO(

                @Schema(description = "Max 50", example = "10") @Min(1) @Max(50) int limit,

                @Schema(example = "0") @Min(0) int pageNumber,

                @Schema(description = "Field to order", example = "updatedAt") String sort,

                @Schema(description = "Order, asc/desc", example = "asc") String order,

                @Schema(description = "Game alias for autocomplete", example = "gtav") String alias,

                @Schema(description = "Developer name for filtering", example = "blizzard-entertainment") String developer,

                @Schema(description = "Game Status", example = "[\"RELEASED\", \"BETA\", \"EARLY_ACCESS\"]") List<GameStatus> status,

                @Schema(description = "List of game genres for filtering", example = "[\"SHOOTER\", \"ARCADE\"]") List<GameGenre> genres,

                @Schema(description = "List of game modes for filtering", example = "[\"SINGLE_PLAYER\", \"MULTIPLAYER\", \"CO_OPERATIVE\"]") List<GameMode> modes,

                @Schema(description = "List of game platforms for filtering", example = "[\"PC_MICROSOFT_WINDOWS\", \"PLAYSTATION_5\"]") List<GamePlatform> platforms,

                @Schema(description = "Release date range start", example = "2020-01-01") LocalDate firstReleaseDate,

                @Schema(description = "Game Types", example = "[\"MAIN_GAME\", \"REMAKE\", \"EXPANSION\", \"REMASTER\"]") List<GameType> types

) {
}
