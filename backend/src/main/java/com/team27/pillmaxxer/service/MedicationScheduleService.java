package com.team27.pillmaxxer.service;

import com.team27.pillmaxxer.model.Medication;
import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.model.Prescription;
import com.team27.pillmaxxer.repositories.MedicationScheduleRepository;
import com.team27.pillmaxxer.repositories.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class MedicationScheduleService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationScheduleRepository scheduleRepository;

    public MedicationScheduleService(PrescriptionRepository prescriptionRepository,
            MedicationScheduleRepository scheduleRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public MedicationSchedule updatePatientSchedule(String patientId) throws ExecutionException, InterruptedException {
        // Get today's date and generate schedule
        LocalDate today = LocalDate.now();
        System.out.println("Generating schedule for patient: " + patientId + " on " + today);
        MedicationSchedule generatedSchedule = generateDailySchedule(patientId, today);
        System.out.println("Generated schedule: " + generatedSchedule);
        System.out.println("Saving Schedule ... ");
        return scheduleRepository.save(generatedSchedule);

        /*
         * TODO - Generate schedules for future dates in the prescription range
         * The current medicationSchedule model only supports a single day's schedule
         */
    }

    public MedicationSchedule generateDailySchedule(String patientId, LocalDate date)
            throws ExecutionException, InterruptedException {
        // Get active prescriptions for the patient
        System.out.println("Finding active prescriptions for patient: " + patientId);
        List<Prescription> prescriptions = prescriptionRepository.findActiveByPatientId(patientId);

        System.out.println("Found " + prescriptions.size() + " active prescriptions");

        System.out.println("Generating schedule for patient: " + patientId + " on " + date);
        // Generate and save the schedule
        MedicationSchedule schedule = scheduleRepository.findByPatientAndDate(patientId, date)
                .orElse(new MedicationSchedule());
        System.out.println("Generated schedule: " + schedule);
        System.out.println("Setting schedule properties");
        schedule.setPatientId(patientId);
        schedule.setDate(date);

        if (schedule.getScheduledDoses() != null) {
            System.out.println("Clearing existing scheduled doses");
            schedule.getScheduledDoses().clear();
        }

        System.out.println("Generating time slots for " + prescriptions.size() + " prescriptions");
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

        System.out.println("Generated " + timeSlots.size() + " time slots");
        System.out.println("Adding " + timeSlots.size() + " time slots to scheduled doses");
        schedule.setScheduledDoses(
                timeSlots.entrySet().stream()
                        .map(e -> new MedicationSchedule.ScheduledDose(e.getKey(), e.getValue()))
                        .collect(Collectors.toList()));

        return schedule;
    }

    private boolean isActiveOnDate(Prescription p, LocalDate date) {
        return p.isActive() &&
                !date.isBefore(p.getStartDate()) &&
                !date.isAfter(p.getEndDate());
    }

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

    public MedicationSchedule getScheduleForDate(String patientId, LocalDate date)
            throws ExecutionException, InterruptedException {
        return scheduleRepository.findByPatientAndDate(patientId, date)
                .orElseGet(() -> {
                    try {
                        return generateDailySchedule(patientId, date);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to generate schedule", e);
                    }
                });
    }
}
