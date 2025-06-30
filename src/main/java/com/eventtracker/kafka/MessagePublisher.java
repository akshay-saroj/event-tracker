package com.eventtracker.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.eventtracker.dto.ScoreData;
import com.eventtracker.exceptions.KafkaPublishingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessagePublisher {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private DeadLetterQueuePublisher deadLetterQueuePublisher;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Retryable(
            value = KafkaPublishingException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void publish(String topic, ScoreData data) {
        try {
            String message = objectMapper.writeValueAsString(data);
            kafkaTemplate.send(topic, message);
            log.info("Published to Kafka: {}", message);
        } catch (Exception e) {
            log.error("Error publishing to Kafka", e);
            throw new KafkaPublishingException("Kafka publish failed for topic: " + topic, e);
        }
    }

    @Recover
    public void recover(KafkaPublishingException e, String topic, ScoreData data) {
        log.error("Max retries reached. Failed to publish to Kafka topic: {}", topic, e);
        deadLetterQueuePublisher.publishToDLQ("event-tracker-dlq", data, e);
    }
}
