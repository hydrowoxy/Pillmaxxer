package com.team27.pillmaxxer.dto;

import com.google.cloud.firestore.DocumentSnapshot;
import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.model.Reminder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

/**
 * Mapper class for converting between Reminder domain objects and Firestore
 * documents.
 */
@Component
public class ReminderMapper {

    /**
     * Converts a Reminder domain object to a Firestore-compatible Map.
     *
     * @param reminder The Reminder domain object to convert.
     * @return A Map representing the Reminder object for Firestore.
     */
    public Map<String, Object> toFirestoreMap(Reminder reminder) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", reminder.getId());
        map.put("userId", reminder.getUserId());
        map.put("patientDeviceToken", reminder.getPatientDeviceToken());
        map.put("date", reminder.getDate().toString());
        map.put("scheduledDose", mapScheduledDoseToFirestore(reminder.getScheduledDose()));

        return map;
    }

    /**
     * Converts a Firestore DocumentSnapshot to a Reminder domain object.
     *
     * @param document The Firestore DocumentSnapshot to convert.
     * @return A Reminder domain object.
     */
    public Reminder fromFirestoreDocument(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }

        Map<String, Object> scheduledDoseMap = (Map<String, Object>) document.get("scheduledDose");

        return new Reminder(
                document.getString("id"),
                document.getString("userId"),
                document.getString("patientDeviceToken"),
                LocalDate.parse((String) document.getString("date")),
                mapFirestoreToScheduledDose(scheduledDoseMap));
    }

    private Map<String, Object> mapScheduledDoseToFirestore(MedicationSchedule.ScheduledDose sd) {
        if (sd == null) {
            return null;
        }

        Map<String, Object> doseMap = new HashMap<>();
        doseMap.put("timeOfDay", sd.getTimeOfDay().toString()); // Convert LocalTime to String
        doseMap.put("medications", sd.getMedications().stream()
                .map(m -> {
                    Map<String, Object> medMap = new HashMap<>();
                    medMap.put("medicationId", m.getMedicationId());
                    medMap.put("medicationName", m.getMedicationName());
                    medMap.put("dosage", m.getDosage());
                    medMap.put("instructions", m.getInstructions());
                    medMap.put("prescriptionId", m.getPrescriptionId());
                    return medMap;
                })
                .collect(Collectors.toList()));
        return doseMap;
    }

    private MedicationSchedule.ScheduledDose mapFirestoreToScheduledDose(Map<String, Object> doseMap) {
        if (doseMap == null) {
            return null;
        }

        return new MedicationSchedule.ScheduledDose(
                LocalTime.parse((String) doseMap.get("timeOfDay")),
                ((List<Map<String, Object>>) doseMap.get("medications")).stream()
                        .map(m -> new MedicationSchedule.MedicationDose(
                                (String) m.get("medicationId"),
                                (String) m.get("medicationName"),
                                (String) m.get("dosage"),
                                (String) m.get("instructions"),
                                (String) m.get("prescriptionId")))
                        .collect(Collectors.toList()));
    }
}