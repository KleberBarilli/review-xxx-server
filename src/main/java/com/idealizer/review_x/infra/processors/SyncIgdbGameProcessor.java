package com.idealizer.review_x.infra.processors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealizer.review_x.application.modules.games.entities.Game;
import com.idealizer.review_x.application.modules.provider.entities.PlatformType;
import com.idealizer.review_x.application.modules.provider.entities.Provider;
import com.idealizer.review_x.infra.libs.twitch.igdb.GameMapper;
import com.idealizer.review_x.infra.libs.twitch.igdb.IgdbConstants;
import com.idealizer.review_x.infra.libs.twitch.igdb.IgdbGameDTO;
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

    @Scheduled(cron = "0 37 14 * * *", zone = "America/Sao_Paulo")
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
            String igdbQuery = String.format("""
                    fields id,name,slug,summary,storyline,first_release_date,total_rating,total_rating_count,genres,
                    game_modes,cover.image_id,screenshots.image_id,platforms,expansions,similar_games,updated_at;
                    where updated_at > %d & version_parent = null & category = 0;
                    sort id asc;
                    limit %d;
                    offset %d;
                    """, updatedSince, PAGE_SIZE, offset);

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
                if (games.isEmpty()) break;

                BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Game.class);
                boolean hasUpdates = false;

                for (IgdbGameDTO dto : games) {
                    Game game = GameMapper.toEntity(dto);

                    Query query = new Query(new Criteria().andOperator(
                            Criteria.where("igdbId").is(game.getIgdbId()),
                            Criteria.where("updatedAt").lt(oneDayAgo)
                    ));
                    boolean exists = mongoTemplate.exists(query, Game.class);

                    if (exists) {
                        Update update = new Update()
                                .set("similarGamesIgdbIds", game.getSimilarGamesIgdbIds())
                                .set("name", game.getName())
                                .set("slug", game.getSlug())
                                .set("summary", game.getSummary())
                                .set("storyline", game.getStoryline())
                                .set("firstReleaseDate", game.getFirstReleaseDate())
                                .set("totalRating", game.getTotalRating())
                                .set("totalRatingCount", game.getTotalRatingCount())
                                .set("genres", game.getGenres())
                                .set("platforms", game.getPlatforms())
                                .set("modes", game.getModes())
                                .set("cover", game.getCover())
                                .set("screenshots", game.getScreenshots())
                                .set("updatedAt", now);

                        bulkOps.updateOne(query, update);
                        hasUpdates = true;
                    }
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
