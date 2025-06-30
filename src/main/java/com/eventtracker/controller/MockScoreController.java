package com.eventtracker.controller;

import com.eventtracker.dto.ScoreData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/mock-api/events")
public class MockScoreController {

    @GetMapping("/{eventId}")
    public ScoreData getScore(@PathVariable String eventId) {
        return new ScoreData(eventId, "0:" + new Random().nextInt(5));
    }
}