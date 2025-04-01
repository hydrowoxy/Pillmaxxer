package com.team27.pillmaxxer.model;

import lombok.Data;

import java.time.LocalDate;

import com.team27.pillmaxxer.model.MedicationSchedule.ScheduledDose;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents a reminder for a medication dose for a patient.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reminder {
    private String id; // Unique ID for the reminder
    private String patientId;
    private String patientDeviceToken; // Could be used in the future for push notifications, etc.
    private LocalDate date;
    private ScheduledDose scheduledDose;
}
