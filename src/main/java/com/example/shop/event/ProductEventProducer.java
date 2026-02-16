package com.example.shop.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventProducer {

    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    public void sendProductEvent(ProductEvent event) {
        String topic = "product-events";
        String key = event.getProductId().toString();

        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Sent product event: {} to topic: {}, partition: {}",
                                event.getEventType(), topic, result.getRecordMetadata()
                                        .partition());
                    } else {
                        log.error("Failed to send product event: {}", event.getEventType());
                    }
                });
    }
}
