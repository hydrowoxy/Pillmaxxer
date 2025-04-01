package com.team27.pillmaxxer.dto;

import com.team27.pillmaxxer.model.Reminder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReminderResponse {
    private Reminder reminder; // The next reminder for the patient
    private Long pollInterval; // Polling frequency in milliseconds
}
