package com.campus.common.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class AuthRedisUtil {

    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";

    private final RedisTemplate<String, Object> redisTemplate;

    public AuthRedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeRefreshToken(Long userId, String token, long expireSeconds) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, token, expireSeconds, TimeUnit.SECONDS);
    }

    public String getRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public boolean validateRefreshToken(Long userId, String token) {
        String storedToken = getRefreshToken(userId);
        return token.equals(storedToken);
    }

    public void removeRefreshToken(Long userId) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }
}
