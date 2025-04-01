package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.service.MedicationScheduleService;

import lombok.extern.java.Log;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST controller for managing patient medication schedules.
 * Handles schedule retrieval and updates based on active prescriptions.
 * 
 * Base endpoint: /api/patients/{userId}/schedules
 */
@RestController
@RequestMapping("/api/patients/{userId}/schedules")
@Log
public class MedicationScheduleController {

    private final MedicationScheduleService scheduleService;

    public MedicationScheduleController(MedicationScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Retrieves a patient's medication schedule for a specific date.
     * If no date is provided, the current date is used.
     * 
     * Example request: GET /api/patients/patient1/schedules?date=2025-03-26
     * 
     * @param userId The user Id to retrieve the schedule for
     * @param date   The date to retrieve the schedule for
     * @return Schedule with 200 OK, 404 if not found, or 500 on error
     */
    @GetMapping
    public ResponseEntity<MedicationSchedule.DailySchedule> getScheduleForDate(
            @PathVariable String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        try {
            if (date == null) {
                date = LocalDate.now();
            }

            return scheduleService.getScheduleForDate(userId, date)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Creates/Updates a patient's medication schedule.
     * 
     * Example Request: POST /api/patients/patient1/schedules/create
     * 
     * @param userId The patient ID to create the schedule for
     * @return Created schedule with 200 OK, or 500 on error
     */
    @PostMapping("/create")
    public ResponseEntity<MedicationSchedule> createSchedule(@PathVariable String userId) {
        try {
            log.info("Creating schedule for patient: " + userId);
            MedicationSchedule schedule = scheduleService.createPatientSchedule(userId);
            log.info("Schedule created successfully for: " + userId);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves a patient's medication schedule for a specific date range.
     * 
     * Example request: GET /api/patients/patient1/schedules/2025-03-26/2025-03-30
     * 
     * @param userId    The patient ID to retrieve the schedule for
     * @param startDate The start date of the range
     * @param endDate   The end date of the range
     * @return Schedule with 200 OK, 404 if not found, or 500 on error
     */
    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<MedicationSchedule> getScheduleForDateRange(
            @PathVariable String userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {

            return scheduleService.getScheduleForDateRange(userId, startDate, endDate)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}