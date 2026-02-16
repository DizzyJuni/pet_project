package com.example.shop.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEventConsumer {

    @KafkaListener(
            topics = "product-events",
            groupId = "shop-analytics-group"
    )
    public void handleProductEvent(ProductEvent event) {
        try {
            switch (event.getEventType()) {
                case PRODUCT_CREATED -> log.info("New product created: {}", event.getProductName());
                case PRODUCT_UPDATED -> log.info("Product updated: {}", event.getProductId());
                case PRODUCT_DELETED -> log.info("Product deleted: {}", event.getProductId());
            }
        } catch (Exception e) {
            log.error(" Failed to process event: {}",
                    event.getEventId(), e);
        }
    }
}
