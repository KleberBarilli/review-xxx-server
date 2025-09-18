package com.idealizer.review_x.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "com.idealizer.review_x.domain.core.tokens")
public class RedisConfig {
}