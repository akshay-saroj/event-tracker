package com.eventtracker.service;

import com.eventtracker.client.ExternalApiClient;
import com.eventtracker.dto.ScoreData;
import com.eventtracker.exceptions.PollingException;
import com.eventtracker.kafka.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EventService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    private final ExternalApiClient client;
    private final MessagePublisher publisher;

    public EventService(ExternalApiClient client, MessagePublisher publisher) {
        this.client = client;
        this.publisher = publisher;
    }

    /**
     * Updates the status of an event. If the event is live, it starts polling for updates.
     * If the event is not live, it stops polling.
     *
     * @param eventId The ID of the event to update.
     * @param isLive  True if the event is live, false otherwise.
     */
    public void updateEventStatus(String eventId, boolean isLive) {
        log.info("Received event status update. Event ID: {}, Status: {}", eventId, isLive ? "LIVE" : "NOT LIVE");

        if (isLive && !tasks.containsKey(eventId)) {
            ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
                    () -> pollAndPublish(eventId), 0, 10, TimeUnit.SECONDS);
            tasks.put(eventId, future);
            log.info("Started polling task for event: {}", eventId);
        } else if (!isLive && tasks.containsKey(eventId)) {
            tasks.get(eventId).cancel(true);
            tasks.remove(eventId);
            log.info("Stopped polling task for event: {}", eventId);
        } else {
            log.debug("No change in polling state for event: {}", eventId);
        }
    }

    /**
     * Polls the external API for score data and publishes it to the Kafka topic.
     *
     * @param eventId The ID of the event to poll.
     */
    private void pollAndPublish(String eventId) {
        try {
            log.debug("Polling external API for event: {}", eventId);
            ScoreData data = client.fetchScore(eventId);
            publisher.publish("event-tracker-topic", data);
        } catch (Exception e) {
            log.error("Polling failed for event: {}", eventId, e);
            throw new PollingException("Polling and publishing failed for event: " + eventId, e);
        }
    }
}