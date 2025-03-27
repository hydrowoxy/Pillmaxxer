package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.model.Patient;
import com.team27.pillmaxxer.dto.PatientRegisterRequest;
import com.team27.pillmaxxer.service.PatientService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/register")
    public ResponseEntity<Patient> registerPatient(@RequestBody PatientRegisterRequest patientRequest) {
        Patient createdPatient;
        try {
            createdPatient = patientService.registerPatient(patientRequest);
            return ResponseEntity.ok(createdPatient);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

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
