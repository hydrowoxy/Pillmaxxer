package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.dto.PrescriptionDto;
import com.team27.pillmaxxer.model.Prescription;
import com.team27.pillmaxxer.service.PrescriptionService;

import lombok.extern.java.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing patient prescriptions.
 * Handles creation and retrieval of medication prescriptions.
 * 
 * Base endpoint: /api/patients/{userId}/prescriptions
 */
@RestController
@RequestMapping("/api/patients/{userId}/prescriptions")
@Log
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * Creates a new prescription for a patient.
     * 
     * Example Request: POST /api/patients/patient1/prescriptions/create
     * 
     * Example Body:
     * {
     * "userId": "patient2",
     * "medicationName": "Amoxicillin",
     * "dosage": "500mg",
     * "startDate": "2025-03-26",
     * "endDate": "2025-03-31",
     * "instructions": "Take with food",
     * "quantity": "2",
     * "frequency": "Once daily"
     * }
     * 
     * @param prescriptionDto Prescription data to create
     * @return Created prescription with 200 OK,
     *         or 500 INTERNAL_SERVER_ERROR if operation fails
     */
    @PostMapping("/create")
    public ResponseEntity<Prescription> createPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        try {
            log.info("Creating prescription...");
            Prescription created = prescriptionService.createPrescription(prescriptionDto);
            log.info("Prescription created: " + created);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves all active prescriptions for a patient.
     * 
     * Example Request: GET /api/patients/patient1/prescriptions
     * 
     * @param userId ID of the patient to retrieve prescriptions for
     * @return List of active prescriptions with 200 OK,
     *         or 500 INTERNAL_SERVER_ERROR if operation fails
     */
    @GetMapping
    public ResponseEntity<List<Prescription>> getPatientPrescriptions(@PathVariable String userId) {
        try {
            log.info("Getting prescriptions for patient: " + userId);
            List<Prescription> prescriptions = prescriptionService.getActivePrescriptionsForPatient(userId);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
