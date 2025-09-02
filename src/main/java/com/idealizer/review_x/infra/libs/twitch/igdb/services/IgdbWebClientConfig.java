package com.idealizer.review_x.infra.libs.twitch.igdb.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class IgdbWebClientConfig {

    @Bean
    RestClient igdbRestClient(IgdbCredentialsService creds) {
        var c = creds.get();
        return RestClient.builder()
                .baseUrl("https://api.igdb.com")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader("Client-ID", c.clientId())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + c.accessToken())
                .build();
    }
}
