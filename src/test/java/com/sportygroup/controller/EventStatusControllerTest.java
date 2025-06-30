package com.sportygroup.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldAcceptValidEventStatusUpdate() throws Exception {
        String payload = "{\"eventId\":\"1234\",\"live\":true}";
        mockMvc.perform(post("/events/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRejectInvalidPayload() throws Exception {
        String payload = "{\"eventId\":\"\"}";
        mockMvc.perform(post("/events/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }
}