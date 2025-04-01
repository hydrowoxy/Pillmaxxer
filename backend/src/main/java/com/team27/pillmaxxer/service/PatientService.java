package com.team27.pillmaxxer.service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuthException;
import com.team27.pillmaxxer.dto.PatientMapper;
import com.team27.pillmaxxer.dto.PatientRegisterRequest;
import com.team27.pillmaxxer.model.Patient;
import com.team27.pillmaxxer.repositories.PatientRepository;
import com.team27.pillmaxxer.service.auth.FirebaseAuthService;

import lombok.extern.java.Log;

@Service
@Log
public class PatientService {

    private final PatientRepository patientRepository;
    private final FirebaseAuthService authService;
    private final PatientMapper patientMapper;

    @Autowired
    public PatientService(PatientRepository patientRepository, FirebaseAuthService authService,
            PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.authService = authService;
        this.patientMapper = patientMapper;
    }

    public Patient registerPatient(PatientRegisterRequest patientRequest)
            throws FirebaseAuthException, ExecutionException, InterruptedException {

        log.info("Creating Firebase user..." + patientRequest);
        String uid = authService.createFirebaseUser(patientRequest.getEmail(), patientRequest.getPassword());
        log.info("Firebase user created: " + uid);

        Patient patientData = new Patient();
        patientData = this.patientMapper.toDomainModel(patientRequest, uid);
        patientData.setUserId(uid);
        log.info("Saving patient data...");
        return patientRepository.save(patientData);
    }

    public Optional<Patient> getPatient(String userId) throws ExecutionException, InterruptedException {
        return patientRepository.findById(userId);
    }
}
