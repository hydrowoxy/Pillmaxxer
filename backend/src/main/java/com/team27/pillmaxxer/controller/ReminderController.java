package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.model.Reminder;
import com.team27.pillmaxxer.service.ReminderService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing medication reminders.
 * Handles reminder retrieval and polling frequency.
 *
 * Base endpoint: /api/patients/{patientId}/reminders
 */
@RestController
@RequestMapping("/api/patients/{patientId}/reminders")
@Log
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    /**
     * Retrieves a patient's medication reminders and frontend polling frequency.
     *
     * Example request: GET /api/patients/patient1/reminders
     *
     * @param patientId The patient ID to retrieve reminders for.
     * @return Reminders and polling frequency with 200 OK, or 500 on error
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getRemindersAndPollingFrequency(@PathVariable String patientId) {
        try {
            Reminder reminder = reminderService.getNextReminder(patientId);
            Long pollInterval = reminderService.getPollingFrequency(reminder);

            Map<String, Object> response = new HashMap<>();
            response.put("reminder", reminder);
            response.put("pollInterval", pollInterval);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.severe("Error retrieving reminders for patient: " + patientId + " - " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
