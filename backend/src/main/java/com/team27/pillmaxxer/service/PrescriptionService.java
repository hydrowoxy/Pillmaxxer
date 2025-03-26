package com.team27.pillmaxxer.service;

import com.team27.pillmaxxer.dto.PrescriptionDto;
import com.team27.pillmaxxer.model.Medication;
import com.team27.pillmaxxer.model.Prescription;
import com.team27.pillmaxxer.repositories.MedicationRepository;
import com.team27.pillmaxxer.repositories.PrescriptionRepository;

import lombok.extern.java.Log;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Log
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
            MedicationRepository medicationRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicationRepository = medicationRepository;
    }

    public Prescription createPrescription(PrescriptionDto prescriptionDto)
            throws ExecutionException, InterruptedException {
        // Find or create medication

        log.info("Attempting to create medication: " + prescriptionDto.getMedicationName());
        Medication medication = medicationRepository.findByName(prescriptionDto.getMedicationName())
                .orElseGet(() -> {
                    Medication newMed = new Medication();
                    newMed.setName(prescriptionDto.getMedicationName());
                    try {
                        return medicationRepository.save(newMed);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create medication", e);
                    }
                });

        // Create prescription
        Prescription prescription = new Prescription();
        prescription.setPatientId(prescriptionDto.getPatientId());
        prescription.setMedicationId(medication.getId());
        prescription.setMedicationName(medication.getName());
        prescription.setDosage(prescriptionDto.getDosage());
        prescription.setStartDate(prescriptionDto.getStartDate());
        prescription.setEndDate(prescriptionDto.getEndDate());
        prescription.setInstructions(prescriptionDto.getInstructions());
        prescription.setFrequency(prescriptionDto.getFrequency());
        prescription.setQuantity(prescriptionDto.getQuantity());
        prescription.setActive(true);

        log.info("Attempting to save prescription...");
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return savedPrescription;
    }

    public List<Prescription> getActivePrescriptionsForPatient(String patientId)
            throws ExecutionException, InterruptedException {
        return prescriptionRepository.findActiveByPatientId(patientId);
    }
}
