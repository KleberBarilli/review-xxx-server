package com.idealizer.review_x.infra.processors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealizer.review_x.domain.core.provider.entities.PlatformType;
import com.idealizer.review_x.domain.core.provider.entities.Provider;
import com.idealizer.review_x.domain.core.provider.repositories.ProviderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.logging.Logger;

@Component
public class UpdateTokenTwitchProcessor {
    private final ProviderRepository providerRepository;
    private static final Logger logger = Logger.getLogger(UpdateTokenTwitchProcessor.class.getName());
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${TWITCH_CLIENT_ID}")
    private String clientId;

    @Value("${TWITCH_CLIENT_SECRET}")
    private String clientSecret;

    public UpdateTokenTwitchProcessor(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")));
    }

    @Scheduled(cron = "0 0 8 1 * *", zone = "America/Sao_Paulo")
    public void updateToken() {
        try {
            logger.info("Updating Twitch token...");

            Provider provider = providerRepository.findByPlatform(PlatformType.TWITCH);

            String url = String.format(
                    "https://id.twitch.tv/oauth2/token?client_id=%s&client_secret=%s&grant_type=client_credentials",
                    URLEncoder.encode(clientId, StandardCharsets.UTF_8),
                    URLEncoder.encode(clientSecret, StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logger.severe("Failed to refresh Twitch token: " + response.body());
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            String accessToken = json.get("access_token").asText();
            long expiresInSeconds = json.get("expires_in").asLong();

            provider.setAccessToken(accessToken);
            provider.setExpiresAt(Instant.now().plusSeconds(expiresInSeconds));
            providerRepository.save(provider);

            logger.info("Twitch token updated successfully.");

        } catch (Exception e) {
            logger.severe("Error updating Twitch token: " + e.getMessage());
        }
    }

}