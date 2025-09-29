package com.idealizer.review_x.infra.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisHealthLogger {
    private static final Logger log = LoggerFactory.getLogger(RedisHealthLogger.class);

    @Bean
    ApplicationRunner redisPingRunner(StringRedisTemplate template) {
        return args -> {
            try {
                var conn = template.getRequiredConnectionFactory().getConnection();
                String pong = conn.ping();
                log.info("Redis PING: {}", pong);
            } catch (Exception e) {
                log.error("Redis PING FAILED (na inicialização)", e);
            }
        };
    }
}