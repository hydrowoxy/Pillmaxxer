package com.team27.pillmaxxer.service;

import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.model.Reminder;
import com.team27.pillmaxxer.repositories.ReminderRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Log
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public Reminder getNextReminder(String patientId) throws ExecutionException, InterruptedException {
        List<Reminder> patientReminders = reminderRepository.findByPatientId(patientId);

        if (patientReminders == null || patientReminders.isEmpty()) {
            return null;
        }

        patientReminders.sort((r1, r2) -> {
            int dateComparison = r1.getDate().compareTo(r2.getDate());
            if (dateComparison != 0) {
                return dateComparison;
            }
            return r1.getScheduledDose().getTimeOfDay().compareTo(r2.getScheduledDose().getTimeOfDay());
        });

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // Iterate through the sorted reminders and find the first one that is after now
        for (Reminder reminder : patientReminders) {
            LocalDateTime reminderDateTime = LocalDateTime.of(reminder.getDate(),
                    reminder.getScheduledDose().getTimeOfDay());
            if (reminderDateTime.isAfter(now)) {
                return reminder;
            }
        }

        return null;
    }

    public long getPollingFrequency(Reminder nextReminder) throws ExecutionException, InterruptedException {
        if (nextReminder == null || nextReminder.getScheduledDose() == null) {
            // No schedule, poll infrequently
            return 18000000; // 5 hours (18000000 ms)
        }

        LocalDate nextReminderDate = nextReminder.getDate();
        LocalTime nextReminderTime = nextReminder.getScheduledDose().getTimeOfDay();

        LocalDateTime nextDoseTime = LocalDateTime.of(nextReminderDate, nextReminderTime);

        Duration duration = Duration.between(LocalDateTime.now(ZoneOffset.UTC), nextDoseTime);
        long milliseconds = duration.toMillis();

        /*
         * Optimize polling frequency based on the time until the next dose to reduce
         * calls to the server and improve performance.
         * In reality, we would use Firebase Cloud Messaging (FCM) to send push
         * notifications
         * BUT here we are using polling to simulate the behavior.
         */
        if (milliseconds > 18000000) { // More than 5 hours
            return 18000000; // 5 hours
        } else if (milliseconds > 3600000) { // More than 1 hour
            return 3600000; // 1 hour
        } else if (milliseconds > 900000) { // More than 15 minutes
            return 900000; // 15 minutes
        } else if (milliseconds > 300000) { // More than 5 minutes
            return 300000; // 5 minutes
        } else if (milliseconds > 60000) { // More than 1 minute
            return 60000; // 1 minute
        } else {
            return 30000; // 30 seconds
        }
    }

    public void createOrUpdateReminders(MedicationSchedule schedule) throws ExecutionException, InterruptedException {
        // Clear old reminders
        List<Reminder> oldReminders = reminderRepository.findByPatientId(schedule.getPatientId());
        for (Reminder reminder : oldReminders) {
            reminderRepository.delete(reminder.getId());
        }

        if (schedule == null || schedule.getDailySchedules() == null || schedule.getDailySchedules().isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        List<Reminder> reminders = new ArrayList<>();

        for (MedicationSchedule.DailySchedule dailySchedule : schedule.getDailySchedules()) {
            for (MedicationSchedule.ScheduledDose dose : dailySchedule.getScheduledDoses()) {
                LocalDateTime doseDateTime = LocalDateTime.of(dailySchedule.getDate(), dose.getTimeOfDay());
                if (doseDateTime.isAfter(now)) {
                    reminders.add(new Reminder(null, schedule.getPatientId(), null, dailySchedule.getDate(), dose));
                }
            }
        }
        for (Reminder reminder : reminders) {
            reminderRepository.save(reminder);
        }
    }
}