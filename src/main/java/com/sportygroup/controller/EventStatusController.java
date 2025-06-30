package com.sportygroup.controller;

import com.sportygroup.dto.EventStatusUpdate;
import com.sportygroup.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventStatusController {

    private final EventService eventService;

    public EventStatusController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/status")
    public ResponseEntity<Void> updateStatus(@RequestBody @Valid EventStatusUpdate update) {
        eventService.updateEventStatus(update.getEventId(), update.getLive());
        return ResponseEntity.ok().build();
    }
}
