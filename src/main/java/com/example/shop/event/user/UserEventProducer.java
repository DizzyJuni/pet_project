package com.example.shop.event.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void sendUserEvent(UserEvent event) {
        String topic = "user-events";
        String key = event.getUserId().toString();

        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) ->
                {
                    if (ex == null) {
                        log.info("Send User event: {} to topic {}, partition {}",
                                event.getUserEventType(), topic, result.getRecordMetadata()
                                        .partition());
                    } else {
                        log.error("Failed to send user event : {}", event.getUserEventType());
                    }
                });
    }
}
