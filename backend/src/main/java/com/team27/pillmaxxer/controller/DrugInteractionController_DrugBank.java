package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.service.DrugInteractionService_DrugBank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that exposes API endpoints for checking drug interactions.
 */
@RestController
@RequestMapping("/api/drug-interactions")
public class DrugInteractionController_DrugBank {
    private final DrugInteractionService_DrugBank drugInteractionService;

    // Constructor to inject the service
    public DrugInteractionController_DrugBank(DrugInteractionService_DrugBank drugInteractionService) {
        this.drugInteractionService = drugInteractionService;
    }

    /**
     * API endpoint to check for dangerous interactions for a specific drug.
     *
     * Example Request:
     * GET /api/drug-interactions/check?drug=Ibuprofen
     *
     * @param drug The drug name (e.g., "Ibuprofen").
     * @return A list of dangerous drug/food interactions.
     */
    @GetMapping("/check")
    public List<String> checkDrugInteractions(@RequestParam String drug) {
        return drugInteractionService.getDangerousInteractions(drug);
    }
}
