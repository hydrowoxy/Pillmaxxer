package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.service.DrugInteractionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drug-interactions")
public class DrugInteractionController {
    private final DrugInteractionService drugInteractionService;

    public DrugInteractionController(DrugInteractionService drugInteractionService) {
        this.drugInteractionService = drugInteractionService;
    }

    /**
     * API to check drug interactions.
     * Example Request: GET /api/drug-interactions/check?drug1=Warfarin&drug2=Aspirin
     */
    @GetMapping("/check")
    public String checkInteraction(@RequestParam String drug1, @RequestParam String drug2) {
        return drugInteractionService.checkInteraction(drug1, drug2);
    }
}
