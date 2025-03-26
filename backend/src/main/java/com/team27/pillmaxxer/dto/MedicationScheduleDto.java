package com.team27.pillmaxxer.dto;

import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.model.MedicationSchedule.ScheduledDose;
import com.google.cloud.firestore.DocumentSnapshot;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

public class MedicationScheduleDto {
    private String id;
    private String patientId;
    private LocalDate date;
    private List<ScheduledDoseDto> scheduledDoses;

    @Data
    @AllArgsConstructor
    public static class ScheduledDoseDto {
        private String timeOfDay;
        private List<MedicationDoseDto> medications;

        public static ScheduledDoseDto fromDomain(ScheduledDose domain) {
            return new ScheduledDoseDto(
                    domain.getTimeOfDay().toString(),
                    domain.getMedications().stream()
                            .map(MedicationDoseDto::fromDomain)
                            .collect(Collectors.toList()));
        }
    }

    @Data
    @AllArgsConstructor
    public static class MedicationDoseDto {
        private String medicationId;
        private String medicationName;
        private String dosage;
        private String instructions;
        private String prescriptionId;

        public static MedicationDoseDto fromDomain(MedicationSchedule.MedicationDose domain) {
            return new MedicationDoseDto(
                    domain.getMedicationId(),
                    domain.getMedicationName(),
                    domain.getDosage(),
                    domain.getInstructions(),
                    domain.getPrescriptionId());
        }
    }

    // Conversion methods
    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("patientId", this.patientId);
        map.put("date", this.date.toString());
        map.put("scheduledDoses", this.scheduledDoses.stream()
                .map(sd -> {
                    Map<String, Object> doseMap = new HashMap<>();
                    doseMap.put("timeOfDay", sd.getTimeOfDay());
                    doseMap.put("medications", sd.getMedications().stream()
                            .map(m -> {
                                Map<String, Object> medMap = new HashMap<>();
                                medMap.put("medicationId", m.getMedicationId());
                                medMap.put("medicationName", m.getMedicationName());
                                medMap.put("dosage", m.getDosage());
                                medMap.put("instructions", m.getInstructions());
                                medMap.put("prescriptionId", m.getPrescriptionId());
                                return medMap;
                            })
                            .collect(Collectors.toList()));
                    return doseMap;
                })
                .collect(Collectors.toList()));
        return map;
    }

    public static MedicationScheduleDto fromFirestoreMap(Map<String, Object> map) {
        MedicationScheduleDto dto = new MedicationScheduleDto();
        dto.setId((String) map.get("id"));
        dto.setPatientId((String) map.get("patientId"));
        dto.setDate(LocalDate.parse((String) map.get("date")));

        List<Map<String, Object>> doses = (List<Map<String, Object>>) map.get("scheduledDoses");
        dto.setScheduledDoses(doses.stream()
                .map(sd -> {
                    List<Map<String, Object>> meds = (List<Map<String, Object>>) sd.get("medications");
                    return new ScheduledDoseDto(
                            (String) sd.get("timeOfDay"),
                            meds.stream()
                                    .map(m -> new MedicationDoseDto(
                                            (String) m.get("medicationId"),
                                            (String) m.get("medicationName"),
                                            (String) m.get("dosage"),
                                            (String) m.get("instructions"),
                                            (String) m.get("prescriptionId")))
                                    .collect(Collectors.toList()));
                })
                .collect(Collectors.toList()));

        return dto;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ScheduledDoseDto> getScheduledDoses() {
        return scheduledDoses;
    }

    public void setScheduledDoses(List<ScheduledDoseDto> scheduledDoses) {
        this.scheduledDoses = scheduledDoses;
    }
}