package com.example.shop.event;

import com.example.shop.enums.EventType;
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
public class ProductEventConsumer {

    private final RedisTemplate<String, String> redisTemplate;

    @KafkaListener(
            topics = "product-events",
            groupId = "shop-analytics-group"
    )
    public void handleProductEvent(ProductEvent event) {
        try {
            switch (event.getEventType()) {
                case PRODUCT_CREATED -> {
                    log.info("New product created: {}", event.getProductName());
                    redisTemplate.opsForValue().increment("stats:total:products");

                    String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                    String dailyKey = "stats:daily:products:" + today;

                    redisTemplate.opsForValue().increment(dailyKey);
                    redisTemplate.expire(dailyKey, 7, TimeUnit.DAYS);
                }
                case PRODUCT_UPDATED -> {
                    log.info("Product updated: {}", event.getProductId());
                    redisTemplate.opsForValue().increment("stats:total:updates");
                }
                case PRODUCT_DELETED -> {
                    log.info("Product deleted: {}", event.getProductId());
                    redisTemplate.opsForValue().decrement("stats:total:products");
                }
            }
            redisTemplate.opsForValue().set(
                    "event:last:" + event.getEventId(),
                    event.getEventType().toString(),
                    1, TimeUnit.DAYS
            );
        } catch (Exception e) {
            log.error(" Failed to process event: {}",
                    event.getEventId(), e);
        }
    }

    @KafkaListener(
            topics = "product-events",
            groupId = "analytics"
    )
    public void handleProductViewed(ProductEvent event) {
        if (event.getEventType() == EventType.PRODUCT_VIEWED) {
            log.info("Product {} have been viewed", event.getProductName());
            redisTemplate.opsForZSet().incrementScore("analytics:top:viewed",
                    event.getProductId().toString(), 1);
            if (event.getCategoryId() != null) {
                redisTemplate.opsForZSet().incrementScore("analytics:top:categories",
                        event.getCategoryId().toString(), 1);
            }
        }
    }
}
