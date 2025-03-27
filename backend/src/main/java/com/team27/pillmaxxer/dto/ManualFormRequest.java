package com.team27.pillmaxxer.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManualFormRequest {
    private String patientId;
    private String medicationId;
    private String medicationName;
    private String dosage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String instructions;
    private String quantity;
    private String frequency;
    private boolean active;
}
