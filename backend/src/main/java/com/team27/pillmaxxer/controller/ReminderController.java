package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.model.Reminder;
import com.team27.pillmaxxer.service.ReminderService;
import com.team27.pillmaxxer.dto.ReminderResponse;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing medication reminders.
 * Handles reminder retrieval and polling frequency.
 *
 * Base endpoint: /api/patients/{userId}/reminders
 */
@RestController
@RequestMapping("/api/patients/{userId}/reminders")
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
     * @param userId The patient ID to retrieve reminders for.
     * @return Reminders and polling frequency with 200 OK, or 500 on error
     */
    @GetMapping
    public ResponseEntity<ReminderResponse> getRemindersAndPollingFrequency(@PathVariable String userId) {
        try {
            Reminder reminder = reminderService.getNextReminder(userId);
            Long pollInterval = reminderService.getPollingFrequency(reminder);

            ReminderResponse response = new ReminderResponse(reminder, pollInterval);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.severe("Error retrieving reminders for patient: " + userId + " - " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
