package com.idealizer.review_x.infra.persistence.mongo.games;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration(proxyBeanMethods = false)
@EnableMongoRepositories(basePackages = "com.idealizer.review_x.domain.game", mongoTemplateRef = "gamesMongoTemplate")
public class GamesMongoConfig {

    @Bean
    public MongoDatabaseFactory gamesDbFactory(@Value("${app.mongodb.games.uri}") String uri) {
        return new SimpleMongoClientDatabaseFactory(uri);
    }

    @Bean(name = "gamesMappingContext") // 🔹 nome explícito (diferente do core)
    public MongoMappingContext gamesMappingContext(
            @Value("${spring.data.mongodb.auto-index-creation:false}") boolean autoIndexCreation,
            MongoCustomConversions conversions) {
        MongoMappingContext ctx = new MongoMappingContext();
        ctx.setAutoIndexCreation(autoIndexCreation);
        ctx.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        return ctx;
    }

    @Bean
    public MappingMongoConverter gamesConverter(
            @Qualifier("gamesDbFactory") MongoDatabaseFactory dbf,
            @Qualifier("gamesMappingContext") MongoMappingContext ctx, // 🔹 QUALIFIER obrigatório
            MongoCustomConversions conversions) {
        MappingMongoConverter conv = new MappingMongoConverter(new DefaultDbRefResolver(dbf), ctx);
        conv.setCustomConversions(conversions);
        conv.setTypeMapper(new DefaultMongoTypeMapper(null));
        conv.afterPropertiesSet();
        return conv;
    }

    @Bean(name = "gamesMongoTemplate")
    public MongoTemplate gamesMongoTemplate(
            @Qualifier("gamesDbFactory") MongoDatabaseFactory dbf,
            @Qualifier("gamesConverter") MappingMongoConverter conv) {
        return new MongoTemplate(dbf, conv);
    }
}
