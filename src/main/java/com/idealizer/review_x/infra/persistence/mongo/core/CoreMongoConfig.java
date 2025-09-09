package com.idealizer.review_x.infra.persistence.mongo.core;

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
@EnableMongoRepositories(basePackages = "com.idealizer.review_x.domain.core", mongoTemplateRef = "coreMongoTemplate")
public class CoreMongoConfig {

    @Bean
    public MongoDatabaseFactory coreDbFactory(@Value("${spring.data.mongodb.uri}") String uri) {
        return new SimpleMongoClientDatabaseFactory(uri);
    }

    @Bean(name = "mongoMappingContext")
    @Primary
    public MongoMappingContext coreMappingContext(
            @Value("${spring.data.mongodb.auto-index-creation:true}") boolean autoIndexCreation,
            MongoCustomConversions conversions) {
        MongoMappingContext ctx = new MongoMappingContext();
        ctx.setAutoIndexCreation(autoIndexCreation);
        ctx.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        return ctx;
    }

    @Bean
    public MappingMongoConverter coreConverter(
            @Qualifier("coreDbFactory") MongoDatabaseFactory dbf,
            @Qualifier("mongoMappingContext") MongoMappingContext ctx, // 🔹 QUALIFIER obrigatório
            MongoCustomConversions conversions) {
        MappingMongoConverter conv = new MappingMongoConverter(new DefaultDbRefResolver(dbf), ctx);
        conv.setCustomConversions(conversions);
        conv.setTypeMapper(new DefaultMongoTypeMapper(null));
        conv.afterPropertiesSet();
        return conv;
    }

    @Bean(name = "coreMongoTemplate")
    public MongoTemplate coreMongoTemplate(
            @Qualifier("coreDbFactory") MongoDatabaseFactory dbf,
            @Qualifier("coreConverter") MappingMongoConverter conv) {
        return new MongoTemplate(dbf, conv);
    }
}
