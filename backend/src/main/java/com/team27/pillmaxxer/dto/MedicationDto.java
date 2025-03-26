package com.team27.pillmaxxer.dto;

import com.google.cloud.firestore.DocumentSnapshot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

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
