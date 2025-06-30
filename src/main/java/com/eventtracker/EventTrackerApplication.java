package com.eventtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class EventTrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventTrackerApplication.class, args);
	}
}
