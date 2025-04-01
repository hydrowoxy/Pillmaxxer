package com.team27.pillmaxxer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

/*
 * This class provides methods to convert a Firestore map to a PrescriptionDto and vice versa.
 */
@Getter
@Setter
public class PrescriptionDto {
    private String id;
    private String userId;
    private String medicationId;
    private String medicationName;
    private String dosage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String instructions;
    private String frequency;
    private String quantity;
    private boolean active;

    // Conversion methods
    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("userId", this.userId);
        map.put("medicationId", this.medicationId);
        map.put("medicationName", this.medicationName);
        map.put("dosage", this.dosage);
        map.put("startDate", this.startDate.toString());
        map.put("endDate", this.endDate.toString());
        map.put("instructions", this.instructions);
        map.put("frequency", this.frequency);
        map.put("active", this.active);
        map.put("quantity", this.quantity);
        return map;
    }

    public static PrescriptionDto fromFirestoreMap(Map<String, Object> map) {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setId((String) map.get("id"));
        dto.setUserId((String) map.get("userId"));
        dto.setMedicationId((String) map.get("medicationId"));
        dto.setMedicationName((String) map.get("medicationName"));
        dto.setDosage((String) map.get("dosage"));
        dto.setStartDate(LocalDate.parse((String) map.get("startDate")));
        dto.setEndDate(LocalDate.parse((String) map.get("endDate")));
        dto.setInstructions((String) map.get("instructions"));
        dto.setFrequency((String) map.get("frequency"));
        dto.setQuantity((String) map.get("quantity"));
        dto.setActive((Boolean) map.get("active"));
        return dto;
    }
}
