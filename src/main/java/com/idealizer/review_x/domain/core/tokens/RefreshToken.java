package com.idealizer.review_x.domain.core.tokens;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.Instant;

@RedisHash("refresh_tokens")
public class RefreshToken {
    @Id
    public String jti;
    public String familyId;
    public String userId;
    public String hash;
    public Instant expiresAt;
    public boolean revoked = false;
    public String replacedBy;
    @TimeToLive
    public Long ttl;
}