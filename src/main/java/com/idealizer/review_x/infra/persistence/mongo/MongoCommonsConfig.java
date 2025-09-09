package com.idealizer.review_x.infra.persistence.mongo;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoCommonsConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {

        return new MongoCustomConversions(Collections.emptyList());
    }

    @Bean
    public MongoMappingContext mongoMappingContext(
            @Value("${spring.data.mongodb.auto-index-creation:true}") boolean autoIndexCreation,
            MongoCustomConversions conversions) {
        MongoMappingContext ctx = new MongoMappingContext();
        ctx.setAutoIndexCreation(autoIndexCreation);

        ctx.setSimpleTypeHolder(conversions.getSimpleTypeHolder());

        return ctx;
    }
}
