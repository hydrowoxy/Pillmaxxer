package com.team27.pillmaxxer.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team27.pillmaxxer.dto.ManualFormRequest;
import com.team27.pillmaxxer.model.Prescription;

@RestController
@RequestMapping("/api/manual-form")
public class ManualFormController {

    /**
     * API to create a prescription via manual form input.
     * Example Request: POST /api/manual-form/upload
     * Request body: {
     * "userId": "patient2",
     * "medicationId": "med-id",
     * "medicationName": "Amoxicillin",
     * "dosage": "500mg",
     * "startDate": "2025-03-26",
     * "endDate": "2025-03-31",
     * "instructions": "Take with food",
     * "quantity": "2",
     * "frequency": "Once daily",
     * "active": true
     * }
     */
    @PostMapping("/upload")
    public Prescription uploadManualForm(@RequestBody ManualFormRequest req) {
        Prescription prescription = new Prescription();
        BeanUtils.copyProperties(req, prescription); // copy from dto
        return prescription;
    }
}
