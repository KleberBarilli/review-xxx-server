package com.idealizer.review_x.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@Configuration
public class MongoConfig {

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context) {
        var converter = new MappingMongoConverter(factory, context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null)); // Remove _class
        return converter;
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory factory, MappingMongoConverter converter) {
        return new MongoTemplate(factory, converter);
    }
}
