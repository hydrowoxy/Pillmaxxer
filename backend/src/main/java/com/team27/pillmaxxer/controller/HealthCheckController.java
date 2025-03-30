package com.team27.pillmaxxer.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    @GetMapping
    public String checkHealth() {
        return "ready to pillmax ;)";
    }
}
