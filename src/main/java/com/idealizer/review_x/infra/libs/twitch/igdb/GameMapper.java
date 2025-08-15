package com.idealizer.review_x.infra.libs.twitch.igdb;

import com.idealizer.review_x.domain.game.entities.*;
import com.idealizer.review_x.domain.game.entities.enums.*;
import com.idealizer.review_x.domain.game.entities.records.GameWebsite;
import com.idealizer.review_x.common.helpers.NormalizeSlugs;
import com.idealizer.review_x.common.helpers.TrailerPicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

public class GameMapper {



    public static Game toEntity(IgdbGameDTO dto) {
        Game game = new Game();
        game.setIgdbId(dto.id());
        game.setParentIgdbId(dto.parentIgdbId());
        game.setName(dto.name());
        String incomingSlug = dto.slug();
        String safeSlug = NormalizeSlugs.normalize(
                (incomingSlug == null || incomingSlug.isBlank()) ? dto.name() : incomingSlug
        );
        game.setSlug(safeSlug);
        Optional.ofNullable(dto.gameStatus())
                .map(GameStatus::fromIgdbStatus)
                .ifPresent(game::setStatus);

        Optional.ofNullable(dto.gameType())
                .map(GameType::fromId)
                .ifPresent(game::setType);
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
        String mainDev = Optional.ofNullable(dto.involvedCompanies())
                .orElse(List.of())
                .stream()
                .filter(ic -> Boolean.TRUE.equals(ic.developer()))
                .map(ic -> ic.company() != null ? ic.company().name() : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        game.setDeveloper(mainDev != null ?  NormalizeSlugs.normalize(mainDev.toLowerCase()) : null);
        String url = TrailerPicker.pickBestTrailerUrl(
                dto.videos(),
                IgdbVideoDTO::name,
                IgdbVideoDTO::video_id
        );
        game.setTrailerUrl(url);
        List<GameWebsite> sites = Optional.ofNullable(dto.websites())
                .orElse(List.of())
                .stream()
                .filter(Objects::nonNull)
                .filter(w -> w.url() != null && !w.url().isBlank() && w.type() != null)
                .map(w -> new GameWebsite(
                        GameWebsiteType.fromId(w.type()),
                      (w.url())
                ))
                .distinct()
                .toList();

        game.setWebsites(sites);
        List<String> engines = Optional.ofNullable(dto.gameEngines())
                .orElse(List.of())
                .stream()
                .filter(Objects::nonNull)
                .map(IgdbGameEngine::name)
                .filter(Objects::nonNull)
                .toList();
        game.setEngines(engines);
        game.setUpdatedAt(dto.updatedAt() != null ? Instant.ofEpochSecond(dto.updatedAt()) : null);
        game.setSimilarGamesIgdbIds(dto.similarGames());

        return game;
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
