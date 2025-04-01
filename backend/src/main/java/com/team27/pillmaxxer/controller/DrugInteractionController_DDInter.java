package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.dto.DrugInteractionResponse;
import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.service.DrugInteractionService_DDInter;

import lombok.extern.java.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients/{userId}/schedules/drug-interactions")
@Log
public class DrugInteractionController_DDInter {

    private final DrugInteractionService_DDInter ddiService;

    public DrugInteractionController_DDInter(DrugInteractionService_DDInter ddiService) {
        this.ddiService = ddiService;
    }

    @PostMapping("/check")
    public ResponseEntity<DrugInteractionResponse> checkInteraction(@RequestBody MedicationSchedule schedule) {
        try {
            log.info("Checking drug interactions for schedule: " + schedule.getId());
            DrugInteractionResponse interactions = ddiService.getInteractionLevel(schedule);
            return ResponseEntity.ok(interactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}