package com.team27.pillmaxxer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/*
 * This class provides methods to convert a MedicationDto to a Firestore map and vice versa.
 */
@Getter
@Setter
public class MedicationDto {
    private String id;
    private String name;

    // Conversion methods
    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("name", this.name);
        return map;
    }

    public static MedicationDto fromFirestoreMap(Map<String, Object> map) {
        MedicationDto dto = new MedicationDto();
        dto.setId((String) map.get("id"));
        dto.setName((String) map.get("name"));
        return dto;
    }
}
