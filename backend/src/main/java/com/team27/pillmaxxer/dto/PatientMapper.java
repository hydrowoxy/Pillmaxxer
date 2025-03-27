package com.team27.pillmaxxer.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.cloud.firestore.DocumentSnapshot;
import com.team27.pillmaxxer.model.Patient;

@Component
public class PatientMapper {

    public Patient toDomainModel(PatientRegisterRequest request, String userId) {
        Patient patient = new Patient();
        patient.setEmail(request.getEmail());
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setDeviceTokens(request.getDeviceTokens());
        return patient;
    }

    public PatientRegisterRequest toDto(Patient patient) {
        PatientRegisterRequest request = new PatientRegisterRequest();
        request.setEmail(patient.getEmail());
        request.setFirstName(patient.getFirstName());
        request.setLastName(patient.getLastName());
        request.setPhoneNumber(patient.getPhoneNumber());
        request.setDateOfBirth(patient.getDateOfBirth());
        request.setDeviceTokens(patient.getDeviceTokens());
        return request;
    }

    public Patient fromFirestoreDocument(DocumentSnapshot document) {
        if (!document.exists()) {
            return null;
        }
        Map<String, Object> fireStoreMap = document.getData();
        Patient patient = new Patient();

        patient.setPatientId((String) fireStoreMap.get("patientId"));
        patient.setUserId((String) fireStoreMap.get("userId"));
        patient.setFirstName((String) fireStoreMap.get("firstName"));
        patient.setLastName((String) fireStoreMap.get("lastName"));
        patient.setEmail((String) fireStoreMap.get("email"));
        patient.setPhoneNumber((String) fireStoreMap.get("phoneNumber"));
        patient.setDateOfBirth((String) fireStoreMap.get("dateOfBirth"));
        patient.setDeviceTokens((List<String>) fireStoreMap.get("deviceTokens"));

        return patient;
    }

    public Map<String, Object> toFirestoreMap(Patient patient) {
        Map<String, Object> map = new HashMap<>();
        map.put("patientId", patient.getPatientId());
        map.put("userId", patient.getUserId());
        map.put("firstName", patient.getFirstName());
        map.put("lastName", patient.getLastName());
        map.put("email", patient.getEmail());
        map.put("phoneNumber", patient.getPhoneNumber());
        map.put("dateOfBirth", patient.getDateOfBirth());
        map.put("deviceTokens", patient.getDeviceTokens());
        return map;
    }
}
