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

import static com.idealizer.review_x.infra.twitch.igdb.IgdbConstants.IGDB_BASE_URL;

@Component
public class SyncIgbdGameProcessor {

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
        System.out.println("SyncIgbdGameProcessor initialized#######################");
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")));
    }

    //@Scheduled(cron = "0 0 2 * * *", zone = "America/Sao_Paulo")

    @Scheduled(cron = "0 * * * * *", zone = "America/Sao_Paulo")
    public void importGames() {
        System.out.println("importGames#######################");
        String queryBuilder = """
                fields id,name,slug,summary,storyline,first_release_date,total_rating,total_rating_count,genres,platforms;
                where total_rating != null & total_rating_count > 50;
                sort total_rating desc;
                limit 7;
                """;



        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IGDB_BASE_URL + "/games"))
                .header("Client-ID", clientId)
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(queryBuilder))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            if (response.statusCode() != 200) {
                System.err.println("Failed to fetch games: " + responseBody);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            List<IgdbGameDTO> games = mapper.readValue(responseBody, new TypeReference<>() {
            });

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
                        .set("updatedAt", Instant.now())
                        .setOnInsert("createdAt", Instant.now());

                bulkOps.upsert(query, update);
            }

            bulkOps.execute();



        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
