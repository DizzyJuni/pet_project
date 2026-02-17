package com.example.shop.event.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    private final RedisTemplate<String, String> redisTemplate;

    @KafkaListener(
            topics = "user-events",
            groupId = "shop-analytics-users"
    )
    public void handleUserEvent(UserEvent event) {
        try {
            switch (event.getUserEventType()) {
                case USER_CREATED -> {
                    log.info("New user created: {}", event.getUserName());
                    redisTemplate.opsForValue().increment("stats:total:users:");

                    String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                    String dailyKey = "stats:daily:users:" + today;

                    redisTemplate.opsForValue().increment(dailyKey);
                    redisTemplate.expire(dailyKey, 7, TimeUnit.DAYS);
                }
                case USER_UPDATED -> {
                    log.info("User updated: {}", event.getUserName());
                    redisTemplate.opsForValue().increment("stats:total:updates:");
                }
                case USER_DELETED -> {
                    log.info("User deleted : {}", event.getUserName());
                    redisTemplate.opsForValue().decrement("stats:total:users:");
                }
                case USER_VIEWED -> {
                    log.info("User viewed: {}", event.getUserName());
                    redisTemplate.opsForZSet().incrementScore("analytics:top:users:",
                            event.getUserId().toString(), 1);
                }
            }
            redisTemplate.opsForValue().set(
                    "event:last:" + event.getEventId(),
                    event.getUserEventType().toString(),
                    1, TimeUnit.DAYS
            );
        } catch (Exception e) {
            log.error(" Failed to process event: {}",
                    event.getEventId(), e);
        }
    }
}
