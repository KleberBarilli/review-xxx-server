package com.idealizer.review_x.infra.libs.twitch.igdb;

import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.entities.GameGenre;
import com.idealizer.review_x.domain.game.entities.GameMode;
import com.idealizer.review_x.domain.game.entities.GamePlatform;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

public class GameMapper {

    public static Game toEntity(IgdbGameDTO dto) {
        Game game = new Game();
        game.setIgdbId(dto.id());
        game.setName(dto.name());
        game.setSlug(dto.slug());
        game.setSummary(dto.summary());
        game.setStoryline(dto.storyline());
        game.setFirstReleaseDate(
                toLocalDate(dto.firstReleaseDate()));
        game.setCover(dto.cover() == null ? null : dto.cover().imageId());
        game.setScreenshots(dto.screenshots() == null
                ? List.of()
                : dto.screenshots().stream()
                        .map(IgdbImageDTO::imageId)
                        .toList());
        game.setTotalRating(dto.totalRating());
        game.setTotalRatingCount(dto.totalRatingCount());
        game.setDlcsIgdbIds(dto.expansions());
        game.setGenres(mapGenreIdsToEnums(
                dto.genres() == null ? List.of()
                        : dto.genres().stream()
                                .filter(Objects::nonNull)
                                .filter(genre -> genre.matches("\\d+"))
                                .map(Integer::parseInt)
                                .toList()));
        game.setPlatforms(mapPlatformIdsToEnums(
                dto.platforms() == null ? List.of()
                        : dto.platforms().stream()
                                .filter(Objects::nonNull)
                                .filter(platform -> platform.matches("\\d+"))
                                .map(Integer::parseInt)
                                .toList()));
        game.setModes(dto.gameModes() == null ? List.of()
                : dto.gameModes().stream()
                        .filter(Objects::nonNull)
                        .filter(mode -> mode.matches("\\d+"))
                        .map(Integer::parseInt)
                        .map(GameMode::fromIgdbId)
                        .toList());
        game.setUpdatedAt(dto.updatedAt() != null ? Instant.ofEpochSecond(dto.updatedAt()) : null);
        game.setSimilarGamesIgdbIds(dto.similarGames());

        return game;
    }

    public static List<Game> toEntityList(List<IgdbGameDTO> dtos) {
        return dtos.stream()
                .map(GameMapper::toEntity)
                .toList();
    }

    private static LocalDate toLocalDate(Long timestamp) {
        if (timestamp == null)
            return null;
        return Instant.ofEpochSecond(timestamp)
                .atZone(ZoneOffset.UTC)
                .toLocalDate();
    }

    public static List<GamePlatform> mapPlatformIdsToEnums(List<Integer> ids) {
        if (ids == null)
            return List.of();

        Set<GamePlatform> platformSet = new HashSet<>();
        boolean hasOther = false;

        for (Integer id : ids) {
            GamePlatform platform = GamePlatform.fromIgdbId(id);
            if (platform == GamePlatform.OTHER) {
                hasOther = true;
            } else {
                platformSet.add(platform);
            }
        }

        if (hasOther) {
            platformSet.add(GamePlatform.OTHER);
        }

        return new ArrayList<>(platformSet);
    }

    public static List<GameGenre> mapGenreIdsToEnums(List<Integer> ids) {
        if (ids == null)
            return List.of();

        Set<GameGenre> genreSet = new HashSet<>();
        boolean hasOther = false;

        for (Integer id : ids) {
            GameGenre genre = GameGenre.fromIgdbId(id);
            if (genre == GameGenre.OTHER) {
                hasOther = true;
            } else {
                genreSet.add(genre);
            }
        }

        if (hasOther) {
            genreSet.add(GameGenre.OTHER);
        }

        return new ArrayList<>(genreSet);
    }
}
