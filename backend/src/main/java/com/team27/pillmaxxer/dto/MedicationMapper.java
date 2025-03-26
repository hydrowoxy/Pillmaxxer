package com.team27.pillmaxxer.dto;

import com.google.cloud.firestore.DocumentSnapshot;
import com.team27.pillmaxxer.model.Medication;
import org.springframework.stereotype.Component;

/*
 * This class is used to map the MedicationDto to the Medication model and vice versa.
 * It also contains a method to convert a Firestore document to a MedicationDto using methods in the MedicationDto class.
 */
@Component
public class MedicationMapper implements FirestoreMapper<MedicationDto, Medication> {

    @Override
    public MedicationDto toDto(Medication model) {
        MedicationDto dto = new MedicationDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        return dto;
    }

    @Override
    public Medication toDomainModel(MedicationDto dto) {
        Medication model = new Medication();
        model.setId(dto.getId());
        model.setName(dto.getName());
        return model;
    }

    @Override
    public MedicationDto fromFirestoreDocument(DocumentSnapshot document) {
        if (!document.exists()) {
            return null;
        }
        return MedicationDto.fromFirestoreMap(document.getData());
    }
}
