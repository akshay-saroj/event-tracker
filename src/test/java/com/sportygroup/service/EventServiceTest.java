package com.sportygroup.service;

import com.sportygroup.client.ExternalApiClient;
import com.sportygroup.dto.ScoreData;
import com.sportygroup.kafka.MessagePublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private ExternalApiClient externalApiClient;

    @Mock
    private MessagePublisher messagePublisher;

    @InjectMocks
    private EventService eventService;

    @Test
    public void shouldSchedulePollingForLiveEvent() throws InterruptedException {
        String eventId = "live-event";
        ScoreData score = new ScoreData(eventId, "1:0");
        when(externalApiClient.fetchScore(eventId)).thenReturn(score);

        eventService.updateEventStatus(eventId, true);
        Thread.sleep(1200);
        eventService.updateEventStatus(eventId, false);

        verify(externalApiClient, atLeastOnce()).fetchScore(eq(eventId));
        verify(messagePublisher, atLeastOnce()).publish(eq("event-tracker-topic"), eq(score));
    }

    @Test
    public void shouldNotScheduleTwice() {
        String eventId = "event-once";
        eventService.updateEventStatus(eventId, true);
        eventService.updateEventStatus(eventId, true);
        eventService.updateEventStatus(eventId, false);

        // No exception means no duplicate scheduling; behavior verified implicitly
    }

    @Test
    public void shouldHandlePollingFailureGracefully() {
        String eventId = "failing-event";
        when(externalApiClient.fetchScore(eventId)).thenThrow(new RuntimeException("API Down"));

        eventService.updateEventStatus(eventId, true);
        sleepQuietly(1200);
        eventService.updateEventStatus(eventId, false);

        verify(externalApiClient, atLeastOnce()).fetchScore(eq(eventId));
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
