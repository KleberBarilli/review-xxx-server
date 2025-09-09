package com.idealizer.review_x.infra.processors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.provider.entities.PlatformType;
import com.idealizer.review_x.domain.provider.entities.Provider;
import com.idealizer.review_x.infra.libs.twitch.igdb.GameMapper;
import com.idealizer.review_x.infra.libs.twitch.igdb.IgdbGameDTO;
import com.idealizer.review_x.infra.processors.utils.Updates;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.idealizer.review_x.infra.libs.twitch.igdb.IgdbConstants.IGDB_BASE_URL;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

@Component
public class SyncIgdbGameProcessor {
    private static final Logger logger = Logger.getLogger(SyncIgdbGameProcessor.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final int PAGE_SIZE = 500;

    @Value("${TWITCH_CLIENT_ID}")
    private String clientId;

    private final MongoTemplate mongoTemplate;

    public SyncIgdbGameProcessor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
        logger.info("SyncIgdbGameProcessor initialized");
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")));
    }

    private String getAccessToken() {
        Query query = new Query(Criteria.where("platform").is(PlatformType.TWITCH));
        Provider provider = mongoTemplate.findOne(query, Provider.class, "providers");
        if (provider == null) {
            throw new IllegalStateException("Provider not found for platform: TWITCH");
        }
        return provider.getAccessToken();
    }

    @Scheduled(cron = "0 01 04 * * *", zone = "America/Sao_Paulo")
    public void syncGames() {
        logger.info("Starting game synchronization from IGDB...");
        String accessToken = getAccessToken();
        logger.info("Access token retrieved successfully.");
        Instant now = Instant.now();

        long updatedSince = now.minus(48, ChronoUnit.HOURS).getEpochSecond();
        Instant oneDayAgo = now.minus(1, ChronoUnit.DAYS);

        int offset = 0;
        int page = 1;

        while (true) {
            String igdbQuery = String.format(
                    """
                             fields id,name,slug,first_release_date,total_rating,total_rating_count,genres,
                            game_modes,cover.image_id,platforms,expansions,updated_at,
                            involved_companies.developer,involved_companies.company.name,
                            videos.name,videos.video_id,game_status,game_type, parent_game;
                                where updated_at > %d & game_type = (0,2,8,9);
                                sort id asc;
                                limit %d;
                                offset %d;
                                """,
                    updatedSince, PAGE_SIZE, offset);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(IGDB_BASE_URL + "/games"))
                    .header("Client-ID", clientId)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(igdbQuery))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    logger.severe("Failed to fetch games: " + response.body());
                    break;
                }

                List<IgdbGameDTO> games = mapper.readValue(response.body(), new TypeReference<>() {
                });
                if (games.isEmpty())
                    break;

                BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Game.class);
                boolean hasUpdates = false;

                for (IgdbGameDTO dto : games) {
                    Game mapped = GameMapper.toEntity(dto);
                    Criteria crit = new Criteria().andOperator(
                            Criteria.where("igdbId").is(mapped.getIgdbId()),
                            new Criteria().orOperator(
                                    Criteria.where("updatedAt").lt(oneDayAgo),
                                    Criteria.where("updatedAt").exists(false)));
                    Query q = new Query(crit);

                    Update update = new Update()
                            .set("name", mapped.getName())
                            .set("type", mapped.getType())
                            .set("status", mapped.getStatus())
                            .set("totalRating", mapped.getTotalRating())
                            .set("totalRatingCount", mapped.getTotalRatingCount())
                            .set("cover", mapped.getCover())
                            .set("updatedAt", now);

                    Updates.setIfNotNull(update, "genres", mapped.getGenres());
                    Updates.setIfNotNull(update, "platforms", mapped.getPlatforms());
                    Updates.setIfNotNull(update, "modes", mapped.getModes());

                    Updates.setIfNotNull(update, "developer", mapped.getDeveloper());
                    Updates.setIfNotNull(update, "trailerUrl", mapped.getTrailerUrl());

                    bulkOps.updateOne(q, update);
                    hasUpdates = true;
                }

                if (hasUpdates) {
                    bulkOps.execute();
                    logger.info("Page " + page + ": Updated existing games.");
                } else {
                    logger.info("Page " + page + ": No existing games to update.");
                }

                offset += PAGE_SIZE;
                page++;

            } catch (Exception e) {
                logger.severe("Error updating games: " + e.getMessage());
                break;
            }

        }
    }
}
