package com.team27.pillmaxxer.model;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    private String id;
    private String patientId;
    private String medicationId;
    private String medicationName;
    private String dosage; // dosage amount for each pill
    private LocalDate startDate;
    private LocalDate endDate;
    private String instructions;
    private String quantity; // number of pills
    private String frequency; // number of times per day
    private boolean active = true;
}