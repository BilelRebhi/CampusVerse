package com.campusverse.event.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Simple endpoint that proves the service is up (Validation 1). */
@RestController
@RequestMapping("/api/events")
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "service", "event-service",
                "microservice", "MS4 - Events",
                "status", "UP");
    }
}
