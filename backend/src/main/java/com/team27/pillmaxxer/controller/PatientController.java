package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.model.Patient;
import com.team27.pillmaxxer.dto.PatientRegisterRequest;
import com.team27.pillmaxxer.service.PatientService;

import lombok.extern.java.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing patients
 * Handles creation and retrieval of patient data.
 * 
 * Base endpoint: /api/patients
 */
@RestController
@RequestMapping("/api/patients")
@Log
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Registers a new patient in the system.
     * 
     * Example Request: POST /api/patients/register
     * 
     * Example Body:
     * {
     * "email": "ayushpatel.8332@gmail.com",
     * "encryptedPassword": "418759249857425",
     * "firstName": "Ayush",
     * "lastName": "Patel",
     * "phoneNumber": "9293840329",
     * "dateOfBirth": "749237493",
     * "deviceTokens": ["43894810983", "47891347928"]
     * }
     * 
     * @param patientRequest Patient data to register
     * @return Created patient with 200 OK, or 500 INTERNAL_SERVER_ERROR if
     *         operation fails
     */
    @PostMapping("/register")
    public ResponseEntity<Patient> registerPatient(@RequestBody PatientRegisterRequest patientRequest) {
        Patient createdPatient;
        try {
            log.info("Registering patient...");
            createdPatient = patientService.registerPatient(patientRequest);
            log.info("Patient registered: " + createdPatient.getEmail());
            return ResponseEntity.ok(createdPatient);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves a patient's data by ID.
     * 
     * Example Request: GET /api/patients/patient1
     * 
     * @param patientId The patient ID to retrieve
     * @return Patient data with 200 OK, 404 if not found, or 500 on error
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable String patientId) {
        try {
            return patientService.getPatient(patientId)
                    .map(patient -> ResponseEntity.ok(patient))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
