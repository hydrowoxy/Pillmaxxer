package com.team27.pillmaxxer.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    @GetMapping
    public Map<String, String> checkHealth() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "ready to pillmax ;)");
        return response;
    }
}
