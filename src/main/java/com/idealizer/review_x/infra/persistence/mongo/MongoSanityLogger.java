package com.idealizer.review_x.infra.persistence.mongo;

import com.mongodb.ConnectionString;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoSanityLogger {
    private static final Logger log = LoggerFactory.getLogger(MongoSanityLogger.class);

    private static String sanitize(String raw) {
        if (raw == null)
            return "<null>";
        var cs = new ConnectionString(raw);
        var hosts = String.join(",", cs.getHosts());
        var db = cs.getDatabase();
        var app = cs.getApplicationName();
        return "mongodb+srv://" + hosts + "/" + (db != null ? db : "<no-db>")
                + (app != null ? "?appName=" + app : "");
    }

    @Bean
    CommandLineRunner logAndAssert(
            Environment env,
            @Qualifier("coreMongoTemplate") MongoTemplate core,
            @Qualifier("gamesMongoTemplate") MongoTemplate games) {
        return args -> {
            String coreUri = env.getProperty("spring.data.mongodb.uri");
            String gamesUri = env.getProperty("app.mongodb.games.uri");

            Document pongCore = core.executeCommand(new Document("ping", 1));
            Document pongGames = games.executeCommand(new Document("ping", 1));

            log.info("[CORE-DB ] name='{}' ping={}", core.getDb().getName(), pongCore.toJson());
            log.info("[GAMES-DB] name='{}' ping={}", games.getDb().getName(), pongGames.toJson());

            if (core.getDb().getName().equals(games.getDb().getName())) {
                throw new IllegalStateException(
                        "Config inválida: CORE e GAMES estão no MESMO database: '" + core.getDb().getName()
                                + "'. Ajuste MONGODB_URI e MONGODB_URI_GAMES para DBs diferentes.");
            }
        };
    }
}
