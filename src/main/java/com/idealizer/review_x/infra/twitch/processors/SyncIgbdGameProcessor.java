package com.idealizer.review_x.infra.twitch.processors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealizer.review_x.infra.twitch.igdb.GameMapper;
import com.idealizer.review_x.infra.twitch.igdb.IgdbGameDTO;
import com.idealizer.review_x.modules.games.entities.Game;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import static com.idealizer.review_x.infra.twitch.igdb.IgdbConstants.IGDB_BASE_URL;

@Component
public class SyncIgbdGameProcessor {

    private static final Logger logger = Logger.getLogger(SyncIgbdGameProcessor.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final int PAGE_SIZE = 500;

    @Value("${TWITCH_CLIENT_ID}")
    private String clientId;

    @Value("${TWITCH_ACCESS_TOKEN}")
    private String accessToken;

    private final MongoTemplate mongoTemplate;

    public SyncIgbdGameProcessor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")));
    }

    @Scheduled(cron = "0 0 2 * * *", zone = "America/Sao_Paulo")
    public void importGames() {
        logger.info("Starting full game import from IGDB...");

        int offset = 0;
        int totalImported = 0;
        int page = 1;

        while (true) {
            String igdbQuery = String.format("""
                    fields id,name,slug,summary,storyline,first_release_date,total_rating,total_rating_count,genres,platforms,dlcs;
                    where total_rating != null & total_rating_count > 50 & version_parent = null & category = 0;
                    sort total_rating desc;
                    limit %d;
                    offset %d;
                    """, PAGE_SIZE, offset);

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
                    logger.severe("Failed to fetch games (offset " + offset + "): " + response.body());
                    break;
                }

                List<IgdbGameDTO> games = mapper.readValue(response.body(), new TypeReference<>() {
                });
                if (games.isEmpty()) {
                    logger.info("No more games to import. Finished.");
                    break;
                }

                BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Game.class);

                for (IgdbGameDTO dto : games) {
                    Game game = GameMapper.toEntity(dto);

                    Query query = new Query(Criteria.where("igdbId").is(game.getIgdbId()));
                    Update update = new Update()
                            .set("name", game.getName())
                            .set("slug", game.getSlug())
                            .set("summary", game.getSummary())
                            .set("storyline", game.getStoryline())
                            .set("firstReleaseDate", game.getFirstReleaseDate())
                            .set("totalRating", game.getTotalRating())
                            .set("totalRatingCount", game.getTotalRatingCount())
                            .set("genres", game.getGenres())
                            .set("platforms", game.getPlatforms())
                            .set("dlcIds", game.getDlcIds())
                            .set("updatedAt", Instant.now())
                            .setOnInsert("createdAt", Instant.now());

                    bulkOps.upsert(query, update);
                }

                bulkOps.execute();
                totalImported += games.size();
                logger.info("Imported page (offset " + offset + ") with " + games.size() + " games.");
                logger.info("Page " + page + ": Imported " + games.size() + " games (Total: " + totalImported + ")");

                offset += PAGE_SIZE;
                page++;

                Thread.sleep(2000);

            } catch (IOException | InterruptedException e) {
                logger.severe("Error importing games: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }

        logger.info("Total games imported/updated: " + totalImported);
    }
}
