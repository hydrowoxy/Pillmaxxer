package com.team27.pillmaxxer.service;

import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.model.Prescription;
import com.team27.pillmaxxer.repositories.MedicationScheduleRepository;
import com.team27.pillmaxxer.repositories.PrescriptionRepository;

import lombok.extern.java.Log;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Log
public class MedicationScheduleService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationScheduleRepository scheduleRepository;
    private final ReminderService reminderService;

    public MedicationScheduleService(PrescriptionRepository prescriptionRepository,
            MedicationScheduleRepository scheduleRepository, ReminderService reminderService) {
        this.prescriptionRepository = prescriptionRepository;
        this.scheduleRepository = scheduleRepository;
        this.reminderService = reminderService;
    }

    /*
     * This function creates the schedule for a given patient.
     * It generates schedules for all days for which there are active prescriptions,
     * either appending to an already existing schedule or creating a new one.
     */
    public MedicationSchedule createPatientSchedule(String userId) throws ExecutionException, InterruptedException {
        log.info("Generating schedule for patient: " + userId);

        // Get active prescriptions for the patient
        List<Prescription> prescriptions = prescriptionRepository.findActiveByuserId(userId);
        log.info("Found " + prescriptions.size() + " active prescriptions");

        // Determine the range of dates to generate schedules for
        List<LocalDate> dates = prescriptions.stream()
                .flatMap(p -> p.getStartDate().datesUntil(p.getEndDate().plusDays(1)))
                .distinct()
                .collect(Collectors.toList());

        log.info("Range of dates: " + dates);

        MedicationSchedule schedule = scheduleRepository.findByuserId(userId);
        log.info("Found schedule: " + schedule);

        if (schedule == null) {
            schedule = new MedicationSchedule();
            schedule.setId(userId + "_schedule");
        }
        schedule.setUserId(userId);

        // Ensure dailySchedules is initialized if it's null
        if (schedule.getDailySchedules() == null) {
            schedule.setDailySchedules(new ArrayList<>());
        }

        for (LocalDate date : dates) {
            // Find or create the DailySchedule for the current date
            MedicationSchedule.DailySchedule dailySchedule = schedule.getDailySchedules().stream()
                    .filter(ds -> ds.getDate().equals(date))
                    .findFirst()
                    .orElse(new MedicationSchedule.DailySchedule(date, new ArrayList<>()));

            // If the daily schedule was not found in the existing list, add it
            if (!schedule.getDailySchedules().contains(dailySchedule)) {
                schedule.getDailySchedules().add(dailySchedule);
            }

            // Clear existing scheduled doses for the current date
            if (dailySchedule.getScheduledDoses() != null) {
                dailySchedule.getScheduledDoses().clear();
            }

            Map<LocalTime, List<MedicationSchedule.MedicationDose>> timeSlots = new TreeMap<>();

            // filter for active prescriptions and generate times for each
            // prescription, a make a medication dose for each time slot
            prescriptions.stream()
                    .filter(p -> isActiveOnDate(p, date))
                    .forEach(p -> generateTimes(p.getFrequency()).forEach(time -> {
                        var dose = new MedicationSchedule.MedicationDose(
                                p.getMedicationId(),
                                p.getMedicationName(),
                                p.getDosage(),
                                p.getInstructions(),
                                p.getId());
                        timeSlots.computeIfAbsent(time, k -> new ArrayList<>()).add(dose);
                    }));

            log.info("Generated " + timeSlots.size() + " time slots for date: " + date);
            dailySchedule.setScheduledDoses(
                    timeSlots.entrySet().stream()
                            .map(e -> new MedicationSchedule.ScheduledDose(e.getKey(), e.getValue()))
                            .collect(Collectors.toList()));
        }

        MedicationSchedule savedSchedule = scheduleRepository.save(schedule);

        log.info("Creating or updating reminders for patient: " + userId);
        reminderService.createOrUpdateReminders(savedSchedule);
        return savedSchedule;
    }

    /*
     * This helper function makes sure that a prescription is active and that the
     * date is within the prescription range.
     */
    private boolean isActiveOnDate(Prescription p, LocalDate date) {
        return p.isActive() &&
                !date.isBefore(p.getStartDate()) &&
                !date.isAfter(p.getEndDate());
    }

    /*
     * This helper function generates the times of day for a given frequency.
     * The times are hardcoded for now, but could be made more dynamic in the
     * future.
     */
    private List<LocalTime> generateTimes(String frequency) {
        return switch (frequency.toLowerCase()) {
            case "once daily" -> List.of(LocalTime.of(9, 0));
            case "twice daily" -> List.of(LocalTime.of(9, 0), LocalTime.of(21, 0));
            case "three times daily" -> List.of(LocalTime.of(8, 0), LocalTime.of(14, 0), LocalTime.of(20, 0));
            case "every 6 hours" ->
                List.of(LocalTime.of(6, 0), LocalTime.of(12, 0), LocalTime.of(18, 0), LocalTime.of(0, 0));
            default -> List.of(LocalTime.of(12, 0));
        };
    }

    /*
     * This function retrieves the schedule for a given patient and date.
     * If no schedule exists for the given date, an empty optional is returned.
     */
    public Optional<MedicationSchedule.DailySchedule> getScheduleForDate(String userId, LocalDate date)
            throws ExecutionException, InterruptedException {
        MedicationSchedule foundSchedule = scheduleRepository.findByuserId(userId);
        if (foundSchedule == null) {
            return Optional.empty();
        }

        return foundSchedule.getDailySchedules().stream()
                .filter(ds -> ds.getDate().equals(date))
                .findFirst();
    }

    /*
     * This function retrieves the schedule for a given patient and date range.
     * If no schedule exists for the given date range, an empty optional is
     * returned.
     */
    public Optional<MedicationSchedule> getScheduleForDateRange(String userId, LocalDate startDate,
            LocalDate endDate) throws ExecutionException, InterruptedException {
        MedicationSchedule foundSchedule = scheduleRepository.findByuserId(userId);

        if (foundSchedule == null) {
            return Optional.empty();
        }

        List<MedicationSchedule.DailySchedule> filteredSchedules = foundSchedule.getDailySchedules().stream()
                .filter(ds -> !ds.getDate().isBefore(startDate) && !ds.getDate().isAfter(endDate))
                .collect(Collectors.toList());

        foundSchedule.setDailySchedules(filteredSchedules);

        return Optional.of(foundSchedule);

    }

    public MedicationSchedule getScheduleForPatient(String userId)
            throws ExecutionException, InterruptedException {
        return scheduleRepository.findByuserId(userId);
    }
}