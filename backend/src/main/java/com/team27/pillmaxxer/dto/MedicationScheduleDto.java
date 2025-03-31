package com.team27.pillmaxxer.dto;

import com.team27.pillmaxxer.model.MedicationSchedule;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/*
 * This class provides methods to convert MedicationSchedule objects to MedicationScheduleDto objects and vice versa.
 * It also provides methods to convert Firestore maps to MedicationScheduleDto objects.
 */

@Getter
@Setter
public class MedicationScheduleDto {
    private String id;
    private String patientId;
    private List<DailyScheduleDto> dailySchedules;

    @Data
    @AllArgsConstructor
    public static class DailyScheduleDto {
        private LocalDate date;
        private List<ScheduledDoseDto> scheduledDoses;

        public static DailyScheduleDto fromDomain(MedicationSchedule.DailySchedule domain) {
            return new DailyScheduleDto(
                    domain.getDate(),
                    domain.getScheduledDoses().stream()
                            .map(ScheduledDoseDto::fromDomain)
                            .collect(Collectors.toList()));
        }
    }

    @Data
    @AllArgsConstructor
    public static class ScheduledDoseDto {
        private String timeOfDay;
        private List<MedicationDoseDto> medications;

        public static ScheduledDoseDto fromDomain(MedicationSchedule.ScheduledDose domain) {
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

    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("patientId", this.patientId);
        map.put("dailySchedules", this.dailySchedules.stream()
                .map(ds -> {
                    Map<String, Object> dailyScheduleMap = new HashMap<>();
                    dailyScheduleMap.put("date", ds.getDate().toString());
                    dailyScheduleMap.put("scheduledDoses", ds.getScheduledDoses().stream()
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
                    return dailyScheduleMap;
                })
                .collect(Collectors.toList()));
        return map;
    }

    public static MedicationScheduleDto fromFirestoreMap(Map<String, Object> map) {
        MedicationScheduleDto dto = new MedicationScheduleDto();
        dto.setId((String) map.get("id"));
        dto.setPatientId((String) map.get("patientId"));

        List<Map<String, Object>> dailySchedules = (List<Map<String, Object>>) map.get("dailySchedules");
        if (dailySchedules != null) {
            dto.setDailySchedules(dailySchedules.stream()
                    .map(ds -> {
                        LocalDate date = LocalDate.parse((String) ds.get("date"));
                        List<Map<String, Object>> doses = (List<Map<String, Object>>) ds.get("scheduledDoses");
                        List<ScheduledDoseDto> scheduledDoseDtos = doses.stream()
                                .map(sd -> {
                                    List<Map<String, Object>> meds = (List<Map<String, Object>>) sd.get("medications");
                                    List<MedicationDoseDto> medicationDoseDtos = meds.stream()
                                            .map(m -> new MedicationDoseDto(
                                                    (String) m.get("medicationId"),
                                                    (String) m.get("medicationName"),
                                                    (String) m.get("dosage"),
                                                    (String) m.get("instructions"),
                                                    (String) m.get("prescriptionId")))
                                            .collect(Collectors.toList());
                                    return new ScheduledDoseDto((String) sd.get("timeOfDay"), medicationDoseDtos);
                                })
                                .collect(Collectors.toList());
                        return new DailyScheduleDto(date, scheduledDoseDtos);
                    })
                    .collect(Collectors.toList()));
        } else {
            dto.setDailySchedules(new ArrayList<>());
        }
        return dto;
    }
}