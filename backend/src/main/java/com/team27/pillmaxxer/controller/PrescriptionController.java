package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.dto.PrescriptionDto;
import com.team27.pillmaxxer.model.Prescription;
import com.team27.pillmaxxer.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Prescription> createPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        try {
            System.out.println("Creating prescription...");
            Prescription created = prescriptionService.createPrescription(prescriptionDto);
            System.out.println("Prescription created: " + created);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Prescription>> getPatientPrescriptions(@PathVariable String patientId) {
        try {
            System.out.println("Getting prescriptions for patient: " + patientId);
            List<Prescription> prescriptions = prescriptionService.getActivePrescriptionsForPatient(patientId);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
