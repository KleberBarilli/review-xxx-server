package com.idealizer.review_x.infra.processors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealizer.review_x.application.modules.provider.entities.PlatformType;
import com.idealizer.review_x.application.modules.provider.entities.Provider;
import com.idealizer.review_x.infra.libs.twitch.igdb.GameMapper;
import com.idealizer.review_x.infra.libs.twitch.igdb.IgdbGameDTO;
import com.idealizer.review_x.application.modules.games.entities.Game;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import static com.idealizer.review_x.infra.libs.twitch.igdb.IgdbConstants.IGDB_BASE_URL;

@Component
public class InsertIgbdGameProcessor {

    private static final Logger logger = Logger.getLogger(InsertIgbdGameProcessor.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final int PAGE_SIZE = 500;

    @Value("${TWITCH_CLIENT_ID}")
    private String clientId;

    private final MongoTemplate mongoTemplate;

    public InsertIgbdGameProcessor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
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

    @Scheduled(cron = "0 0 2 * * *", zone = "America/Sao_Paulo")

    public void importGames() {

        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "igdb_id")).limit(1);
        Game latestGame = mongoTemplate.findOne(query, Game.class);
        Long lastGameId = latestGame != null ? latestGame.getIgdbId() : 0;
        logger.info("Starting full game insert from IGDB...");
        logger.info("Last game ID in database: " + lastGameId);
        String accessToken = getAccessToken();
        logger.info("Access token retrieved successfully.");
        Instant now = Instant.now();

        int offset = 0;
        int page = 1;

        while (true) {
            String igdbQuery = String.format("""
                fields id,name,slug,summary,storyline,first_release_date,total_rating,total_rating_count,genres,
                game_modes,cover.image_id,screenshots.image_id,platforms,expansions,similar_games,updated_at;
                where version_parent = null & category = 0 & id > %d;
                sort id asc;
                limit %d;
                offset %d;
                """,lastGameId, PAGE_SIZE, offset);

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

                List<IgdbGameDTO> games = mapper.readValue(response.body(), new TypeReference<>() {});
                if (games.isEmpty()) break;

                BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Game.class);

                boolean hasInserts = false;
                for (IgdbGameDTO dto : games) {
                    Game game = GameMapper.toEntity(dto);
                    boolean exists = mongoTemplate.exists(
                            new Query(Criteria.where("igdb_id").is(game.getIgdbId())), Game.class
                    );
                    if (!exists) {
                        game.setCreatedAt(now);
                        game.setUpdatedAt(now);
                        bulkOps.insert(game);
                        hasInserts = true;
                    }
                }

                if (hasInserts) {
                    bulkOps.execute();
                    logger.info("Page " + page + ": Inserted new games.");
                } else {
                    logger.info("Page " + page + ": No new games to insert.");
                }
                offset += PAGE_SIZE;
                page++;

            } catch (Exception e) {
                logger.severe("Error inserting new games: " + e.getMessage());
                break;
            }
        }

        logger.info("Finished IGDB new game insert.");
    }
}

