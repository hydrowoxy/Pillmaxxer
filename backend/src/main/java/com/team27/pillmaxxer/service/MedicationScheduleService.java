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

    public MedicationScheduleService(PrescriptionRepository prescriptionRepository,
            MedicationScheduleRepository scheduleRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.scheduleRepository = scheduleRepository;
    }

    /*
     * This function create the schedule for a given patient
     * Currently it only generates a schedule for the current date
     * TODO - This needs to be extended to generate schedules for future dates
     */
    public MedicationSchedule createPatientSchedule(String patientId) throws ExecutionException, InterruptedException {
        // Get today's date and generate schedule
        LocalDate today = LocalDate.now();
        MedicationSchedule generatedSchedule = generateDailySchedule(patientId, today);
        log.info("Saving Schedule ... ");
        return scheduleRepository.save(generatedSchedule);

        /*
         * TODO - Generate schedules for future dates in the prescription range
         * The current medicationSchedule model only supports a single day's schedule
         */
    }

    /*
     * This function generates a daily schedule for a given patient and date
     * First it retrieves all active prescriptions for the patient
     * Then it checks if a schedule already exists for the given date and patient
     * If a schedule does not exist, it generates a new schedule
     * If a schedule does exist, it first clears the existing doses and then
     * generates a new schedule
     * 
     * For all active prescriptions, it generates the times of day for each
     * prescription based on the frequency
     * Each time slot is mapped to a list of medication doses
     * this map is then converted to a list of scheduled doses and saved in the
     * schedule
     */
    public MedicationSchedule generateDailySchedule(String patientId, LocalDate date)
            throws ExecutionException, InterruptedException {
        // Get active prescriptions for the patient
        log.info("Finding active prescriptions for patient: " + patientId);
        List<Prescription> prescriptions = prescriptionRepository.findActiveByPatientId(patientId);

        log.info("Found " + prescriptions.size() + " active prescriptions");

        log.info("Generating schedule for patient: " + patientId + " on " + date);
        // Generate and save the schedule
        MedicationSchedule schedule = scheduleRepository.findByPatientAndDate(patientId, date)
                .orElse(new MedicationSchedule());
        schedule.setPatientId(patientId);
        schedule.setDate(date);

        if (schedule.getScheduledDoses() != null) {
            schedule.getScheduledDoses().clear();
        }

        Map<LocalTime, List<MedicationSchedule.MedicationDose>> timeSlots = new TreeMap<>();

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

        log.info("Generated " + timeSlots.size() + " time slots");
        schedule.setScheduledDoses(
                timeSlots.entrySet().stream()
                        .map(e -> new MedicationSchedule.ScheduledDose(e.getKey(), e.getValue()))
                        .collect(Collectors.toList()));

        return schedule;
    }

    /*
     * This helper function makes sure that a prescription is active and that the
     * date is within the prescription range
     */
    private boolean isActiveOnDate(Prescription p, LocalDate date) {
        return p.isActive() &&
                !date.isBefore(p.getStartDate()) &&
                !date.isAfter(p.getEndDate());
    }

    /*
     * This helper function generates the times of day for a given frequency
     * The times are hardcoded for now, but could be made more dynamic in the future
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
     * This function retrieves the schedule for a given patient and date
     * If no schedule exists for the given date, an empty optional is returned
     */
    public Optional<MedicationSchedule> getScheduleForDate(String patientId, LocalDate date)
            throws ExecutionException, InterruptedException {
        return scheduleRepository.findByPatientAndDate(patientId, date);
    }
}
