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

    public Reminder getNextReminder(String userId) throws ExecutionException, InterruptedException {
        List<Reminder> patientReminders = reminderRepository.findByuserId(userId);

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

        LocalDateTime now = LocalDateTime.now(); // Simplified: no time zone

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
            return 18000000; // 5 hours (18000000 ms)
        }

        LocalDate nextReminderDate = nextReminder.getDate();
        LocalTime nextReminderTime = nextReminder.getScheduledDose().getTimeOfDay();

        LocalDateTime nextDoseTime = LocalDateTime.of(nextReminderDate, nextReminderTime);

        Duration duration = Duration.between(LocalDateTime.now(), nextDoseTime); // Simplified: no time zone
        long milliseconds = duration.toMillis();

        if (milliseconds > 18000000) {
            return 18000000;
        } else if (milliseconds > 3600000) {
            return 3600000;
        } else if (milliseconds > 900000) {
            return 900000;
        } else if (milliseconds > 300000) {
            return 300000;
        } else if (milliseconds > 60000) {
            return 60000;
        } else {
            return 5000;
        }
    }

    public void createOrUpdateReminders(MedicationSchedule schedule) throws ExecutionException, InterruptedException {
        List<Reminder> oldReminders = reminderRepository.findByuserId(schedule.getUserId());
        for (Reminder reminder : oldReminders) {
            reminderRepository.delete(reminder.getId());
        }

        if (schedule == null || schedule.getDailySchedules() == null || schedule.getDailySchedules().isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now(); // Simplified: no time zone
        List<Reminder> reminders = new ArrayList<>();

        for (MedicationSchedule.DailySchedule dailySchedule : schedule.getDailySchedules()) {
            for (MedicationSchedule.ScheduledDose dose : dailySchedule.getScheduledDoses()) {
                LocalDateTime doseDateTime = LocalDateTime.of(dailySchedule.getDate(), dose.getTimeOfDay());
                if (doseDateTime.isAfter(now)) {
                    reminders.add(new Reminder(null, schedule.getUserId(), null, dailySchedule.getDate(), dose));
                }
            }
        }
        for (Reminder reminder : reminders) {
            reminderRepository.save(reminder);
        }
    }
}