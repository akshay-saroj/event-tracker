package com.eventtracker.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.eventtracker.dto.ScoreData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadLetterQueuePublisher {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishToDLQ(String topic, ScoreData data, Exception originalException) {
        try {
            String message = objectMapper.writeValueAsString(data);
            kafkaTemplate.send(topic, message);
            log.warn("Published to DLQ: {} due to error: {}", message, originalException.getMessage());
        } catch (Exception e) {
            log.error("Failed to publish to DLQ", e);
        }
    }
}