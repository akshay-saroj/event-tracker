package com.sportygroup.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sportygroup.dto.ScoreData;
import com.sportygroup.kafka.DeadLetterQueuePublisher;
import com.sportygroup.kafka.MessagePublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.*;


@SpringBootTest
public class MessagePublisherIntegrationTest {

    @Autowired
    private MessagePublisher messagePublisher;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private DeadLetterQueuePublisher deadLetterQueuePublisher;

    @Test
    public void shouldSendToDLQAfterRetriesExhausted() throws JsonProcessingException {
        ScoreData data = new ScoreData("123", "error");

        doThrow(new RuntimeException("Kafka Down"))
                .when(kafkaTemplate).send(anyString(), anyString());

        // The exception is handled internally by Spring Retry -> @Recover
        // So no exception will actually be thrown here
        messagePublisher.publish("event-tracker-topic", data);

        // Verify DLQ was used after retry failure
        verify(deadLetterQueuePublisher, timeout(5000)).publishToDLQ(eq("event-tracker-dlq"), eq(data), any());
    }
}
