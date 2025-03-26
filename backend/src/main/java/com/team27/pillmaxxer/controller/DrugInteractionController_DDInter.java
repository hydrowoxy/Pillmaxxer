package com.team27.pillmaxxer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team27.pillmaxxer.service.DrugInteractionService_DDInter;

@RestController
@RequestMapping("/api/ddi")
public class DrugInteractionController_DDInter {

    private final DrugInteractionService_DDInter ddiService;

    public DrugInteractionController_DDInter(DrugInteractionService_DDInter ddiService) {
        this.ddiService = ddiService;
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkInteraction(
            @RequestParam String drug1,
            @RequestParam String drug2) {
        return ddiService.getInteractionLevel(drug1, drug2)
                .map(level -> ResponseEntity.ok("Interaction Severity: " + level))
                .orElse(ResponseEntity.ok("No known interaction found."));
    }
}
