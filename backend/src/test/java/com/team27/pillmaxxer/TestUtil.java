package com.team27.pillmaxxer;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.team27.pillmaxxer.model.Prescription;

public class TestUtil {

    public static List<Prescription> createMockPrescriptions(String patientId, LocalDate testDate) {
        Prescription prescription1 = new Prescription();
        prescription1.setId("rx1");
        prescription1.setPatientId(patientId);
        prescription1.setMedicationId("med1");
        prescription1.setMedicationName("Medication 1");
        prescription1.setDosage("10mg");
        prescription1.setInstructions("Take with food");
        prescription1.setFrequency("twice daily");
        prescription1.setQuantity("2");
        prescription1.setActive(true);
        prescription1.setStartDate(testDate.minusDays(1));
        prescription1.setEndDate(testDate.plusDays(30));

        Prescription prescription2 = new Prescription();
        prescription2.setId("rx2");
        prescription2.setPatientId(patientId);
        prescription2.setMedicationId("med2");
        prescription2.setMedicationName("Medication 2");
        prescription2.setDosage("5mg");
        prescription2.setInstructions("Take before bed");
        prescription2.setFrequency("once daily");
        prescription2.setQuantity("1");
        prescription2.setActive(true);
        prescription2.setStartDate(testDate.minusDays(1));
        prescription2.setEndDate(testDate.plusDays(30));

        return Arrays.asList(prescription1, prescription2);
    }
}
