package com.partha.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GuardrailService {

    private final StringRedisTemplate redisTemplate;

    // Horizontal Cap (max 100 bot replies per post)
    public void checkHorizontalCap(Long postId) {
        String key = "post:" + postId + ":bot_count";

        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count > 100) {
            throw new RuntimeException("Bot reply limit exceeded (100)");
        }
    }

    //  Vertical Cap (max depth 20)
    public void checkVerticalCap(int depthLevel) {
        if (depthLevel > 20) {
            throw new RuntimeException("Comment depth exceeded (20)");
        }
    }

    // Cooldown Cap (1 interaction per 10 min)
    public void checkCooldown(Long botId, Long humanId) {
        String key = "cooldown:bot_" + botId + ":human_" + humanId;

        Boolean exists = redisTemplate.hasKey(key);

        if (Boolean.TRUE.equals(exists)) {
            throw new RuntimeException("Cooldown active. Try after 10 minutes.");
        }

        // Set key with TTL (10 minutes)
        redisTemplate.opsForValue().set(key, "1", 10, TimeUnit.MINUTES);
    }
}