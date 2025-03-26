package com.team27.pillmaxxer.controller;

import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.service.MedicationScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/patients/{patientId}/schedules")
public class MedicationScheduleController {

    private final MedicationScheduleService scheduleService;

    public MedicationScheduleController(MedicationScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<MedicationSchedule> getSchedule(
            @PathVariable String patientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        try {
            if (date == null) {
                date = LocalDate.now();
            }

            MedicationSchedule schedule = scheduleService.getScheduleForDate(patientId, date);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<MedicationSchedule> updateSchedule(@PathVariable String patientId) {
        try {
            System.out.println("Updating schedule for patient: " + patientId);
            MedicationSchedule schedule = scheduleService.updatePatientSchedule(patientId);
            System.out.println("Schedule updated");
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}