package com.team27.pillmaxxer.model;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents a schedule of medications for a patient on a given date.
 * May need to change to support multiple days of schedules.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicationSchedule {
    private String id;
    private String userId;
    private List<DailySchedule> dailySchedules;

    @Data
    @AllArgsConstructor
    public static class DailySchedule {
        private LocalDate date;
        private List<ScheduledDose> scheduledDoses;
    }

    @Data
    @AllArgsConstructor
    public static class ScheduledDose {
        private LocalTime timeOfDay;
        private List<MedicationDose> medications;
    }

    @Data
    @AllArgsConstructor
    public static class MedicationDose {
        private String medicationId;
        private String medicationName;
        private String dosage;
        private String instructions;
        private String prescriptionId;
    }
}