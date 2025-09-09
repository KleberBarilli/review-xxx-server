package com.idealizer.review_x.infra.persistence.mongo.core;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.idealizer.review_x.domain", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.idealizer\\.review_x\\.domain\\.games?\\.repositories\\..*"), mongoTemplateRef = "coreMongoTemplate")
public class CoreMongoConfig {

    @Bean(name = "coreMongoClient")
    @Primary
    public MongoClient coreMongoClient(@Value("${spring.data.mongodb.uri}") String coreUri) {
        return MongoClients.create(coreUri);
    }

    @Bean(name = "coreMongoDbFactory")
    @Primary
    public MongoDatabaseFactory coreMongoDbFactory(
            @Qualifier("coreMongoClient") MongoClient client,
            @Value("${spring.data.mongodb.uri}") String coreUri) {
        String db = new ConnectionString(coreUri).getDatabase();
        if (db == null || db.isBlank()) {
            throw new IllegalStateException("MONGODB_URI precisa incluir o nome do database (…/meu_db)");
        }
        return new SimpleMongoClientDatabaseFactory(client, db);
    }

    @Bean(name = "coreMongoConverter")
    @Primary
    public MappingMongoConverter coreMongoConverter(
            @Qualifier("coreMongoDbFactory") MongoDatabaseFactory factory,
            MongoMappingContext mappingContext,
            MongoCustomConversions conversions) {
        MappingMongoConverter conv = new MappingMongoConverter(new DefaultDbRefResolver(factory), mappingContext);
        conv.setTypeMapper(new DefaultMongoTypeMapper(null)); // remove _class
        conv.setCustomConversions(conversions);
        conv.afterPropertiesSet();
        return conv;
    }

    @Bean(name = "coreMongoTemplate")
    @Primary
    public MongoTemplate coreMongoTemplate(
            @Qualifier("coreMongoDbFactory") MongoDatabaseFactory factory,
            @Qualifier("coreMongoConverter") MappingMongoConverter converter) {
        return new MongoTemplate(factory, converter);
    }
}
