package com.sportygroup.client;

import com.sportygroup.dto.ScoreData;
import com.sportygroup.exceptions.ScoreFetchException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetches score data for a given event ID from the external API.
     *
     * @param eventId The ID of the event to fetch score data for.
     * @return ScoreData object containing the score information.
     */
    public ScoreData fetchScore(String eventId) {
        try {
            String url = "http://localhost:8080/mock-api/events/" + eventId;
            return restTemplate.getForObject(url, ScoreData.class);
        } catch (RestClientException e) {
            throw new ScoreFetchException("Failed to fetch score for eventId: " + eventId, e);
        }
    }
}