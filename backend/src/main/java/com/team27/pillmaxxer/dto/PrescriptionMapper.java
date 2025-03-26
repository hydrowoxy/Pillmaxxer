package com.team27.pillmaxxer.dto;

import com.google.cloud.firestore.DocumentSnapshot;
import com.team27.pillmaxxer.model.Prescription;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper implements FirestoreMapper<PrescriptionDto, Prescription> {

    @Override
    public PrescriptionDto toDto(Prescription model) {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setId(model.getId());
        dto.setPatientId(model.getPatientId());
        dto.setMedicationId(model.getMedicationId());
        dto.setMedicationName(model.getMedicationName());
        dto.setDosage(model.getDosage());
        dto.setStartDate(model.getStartDate());
        dto.setEndDate(model.getEndDate());
        dto.setInstructions(model.getInstructions());
        dto.setFrequency(model.getFrequency());
        dto.setQuantity(model.getQuantity());
        dto.setActive(model.isActive());
        return dto;
    }

    @Override
    public Prescription toDomainModel(PrescriptionDto dto) {
        Prescription model = new Prescription();
        model.setId(dto.getId());
        model.setPatientId(dto.getPatientId());
        model.setMedicationId(dto.getMedicationId());
        model.setMedicationName(dto.getMedicationName());
        model.setDosage(dto.getDosage());
        model.setStartDate(dto.getStartDate());
        model.setEndDate(dto.getEndDate());
        model.setInstructions(dto.getInstructions());
        model.setFrequency(dto.getFrequency());
        model.setQuantity(dto.getQuantity());
        model.setActive(dto.isActive());
        return model;
    }

    @Override
    public PrescriptionDto fromFirestoreDocument(DocumentSnapshot document) {
        if (!document.exists()) {
            return null;
        }
        return PrescriptionDto.fromFirestoreMap(document.getData());
    }
}