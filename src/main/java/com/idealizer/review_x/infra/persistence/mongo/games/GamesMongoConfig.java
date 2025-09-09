package com.idealizer.review_x.infra.persistence.mongo.games;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableConfigurationProperties(GamesMongoConfig.GamesMongoProperties.class)
@EnableMongoRepositories(basePackages = "com.idealizer.review_x.domain.game.repositories", mongoTemplateRef = "gamesMongoTemplate")
public class GamesMongoConfig {

    @Bean(name = "gamesMongoClient")
    public MongoClient gamesMongoClient(GamesMongoProperties p) {
        return MongoClients.create(p.getUri());
    }

    @Bean(name = "gamesMongoDbFactory")
    public MongoDatabaseFactory gamesMongoDbFactory(
            @Qualifier("gamesMongoClient") MongoClient client,
            GamesMongoProperties p) {
        String db = new ConnectionString(p.getUri()).getDatabase();
        if (db == null || db.isBlank()) {
            throw new IllegalStateException("MONGODB_URI_GAMES precisa incluir o nome do database (…/meu_db)");
        }
        return new SimpleMongoClientDatabaseFactory(client, db);
    }

    @Bean(name = "gamesMongoConverter")
    public MappingMongoConverter gamesMongoConverter(
            @Qualifier("gamesMongoDbFactory") MongoDatabaseFactory factory,
            MongoMappingContext mappingContext,
            MongoCustomConversions conversions) {
        MappingMongoConverter conv = new MappingMongoConverter(new DefaultDbRefResolver(factory), mappingContext);
        conv.setTypeMapper(new DefaultMongoTypeMapper(null)); // remove _class
        conv.setCustomConversions(conversions);
        conv.afterPropertiesSet();
        return conv;
    }

    @Bean(name = "gamesMongoTemplate")
    public MongoTemplate gamesMongoTemplate(
            @Qualifier("gamesMongoDbFactory") MongoDatabaseFactory factory,
            @Qualifier("gamesMongoConverter") MappingMongoConverter converter) {
        return new MongoTemplate(factory, converter);
    }

    @ConfigurationProperties(prefix = "app.mongodb.games")
    public static class GamesMongoProperties {
        private String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }
}
