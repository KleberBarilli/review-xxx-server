package com.idealizer.review_x.infra.libs.twitch.igdb.services;

import com.idealizer.review_x.domain.core.provider.entities.PlatformType;
import com.idealizer.review_x.domain.core.provider.entities.Provider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IgdbCredentialsService {

    private final MongoTemplate coreMongo;
    private final String envClientId;
    private final String envAccessToken;

    public record Credentials(String clientId, String accessToken) {}

    public IgdbCredentialsService(
            @Qualifier("coreMongoTemplate") MongoTemplate coreMongo,
            @Value("${TWITCH_CLIENT_ID:}") String envClientId,
            @Value("${TWITCH_ACCESS_TOKEN:}") String envAccessToken
    ) {
        this.coreMongo = coreMongo;
        this.envClientId = envClientId;
        this.envAccessToken = envAccessToken;
    }

    public Credentials get() {
        Query q = new Query(Criteria.where("platform").is(PlatformType.TWITCH));
        Provider provider = coreMongo.findOne(q, Provider.class, "providers");

        String clientId = Optional.ofNullable(provider)
                .map(Provider::getClientId)
                .filter(s -> !s.isBlank())
                .orElse(envClientId);

        String accessToken = Optional.ofNullable(provider)
                .map(Provider::getAccessToken)
                .filter(s -> !s.isBlank())
                .orElse(envAccessToken);

        if (clientId == null || clientId.isBlank())
            throw new IllegalStateException("Missing IGDB/Twitch Client-ID (provider or ENV).");
        if (accessToken == null || accessToken.isBlank())
            throw new IllegalStateException("Missing IGDB/Twitch Access Token (provider or ENV).");

        return new Credentials(clientId, accessToken);
    }

    public String getClientId() { return get().clientId(); }
    public String getAccessToken() { return get().accessToken(); }
}
