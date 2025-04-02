package com.team27.pillmaxxer.service;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.team27.pillmaxxer.dto.DrugInteractionResponse;
import com.team27.pillmaxxer.model.MedicationSchedule;

@Service
@Log
public class DrugInteractionService_DDInter {

    // Store unique interactions
    private final Set<String> seenPairs = new HashSet<>();
    private final List<DrugInteractionResponse.DrugInteraction> allInteractions = new ArrayList<>();

    // Directory of your CSV files inside /resources/DrugDatabase
    private final String[] fileNames = {
            "ddinter_downloads_code_A.csv",
            "ddinter_downloads_code_B.csv",
            "ddinter_downloads_code_D.csv",
            "ddinter_downloads_code_H.csv",
            "ddinter_downloads_code_L.csv",
            "ddinter_downloads_code_P.csv",
            "ddinter_downloads_code_R.csv",
            "ddinter_downloads_code_V.csv"
    };

    @PostConstruct
    public void loadAllCSVs() {
        for (String fileName : fileNames) {
            String fullPath = "DrugDatabase/" + fileName;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fullPath)),
                    StandardCharsets.UTF_8))) {

                String line;
                reader.readLine(); // Skip header
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",", -1);
                    if (tokens.length >= 5) {
                        String a = tokens[1].trim().toLowerCase();
                        String b = tokens[3].trim().toLowerCase();
                        String level = tokens[4].trim();

                        String pairKey = String.join("::",
                                a.compareTo(b) <= 0 ? a : b,
                                a.compareTo(b) <= 0 ? b : a);

                        if (!seenPairs.contains(pairKey)) {
                            seenPairs.add(pairKey);
                            allInteractions.add(new DrugInteractionResponse.DrugInteraction(a, b, level));
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading file: " + fileName);
                e.printStackTrace();
            }
        }
    }

    public DrugInteractionResponse getInteractionLevel(MedicationSchedule schedule) {
        if (schedule == null || schedule.getDailySchedules() == null) {
            return new DrugInteractionResponse(Collections.emptyList());
        }

        Set<String> uniqueMedications = new HashSet<>();
        List<DrugInteractionResponse.DrugInteraction> foundInteractions = new ArrayList<>();

        for (MedicationSchedule.DailySchedule dailySchedule : schedule.getDailySchedules()) {
            if (dailySchedule.getScheduledDoses() != null) {
                for (MedicationSchedule.ScheduledDose scheduledDose : dailySchedule.getScheduledDoses()) {
                    if (scheduledDose.getMedications() != null) {
                        for (MedicationSchedule.MedicationDose medicationDose : scheduledDose.getMedications()) {
                            uniqueMedications.add(medicationDose.getMedicationName().toLowerCase());
                        }
                    }
                }
            }
        }

        List<String> medicationList = new ArrayList<>(uniqueMedications);

        log.info("Unique medications: " + medicationList);

        for (int i = 0; i < medicationList.size(); i++) {
            for (int j = i + 1; j < medicationList.size(); j++) {
                String drug1 = medicationList.get(i);
                String drug2 = medicationList.get(j);

                Optional<DrugInteractionResponse.DrugInteraction> interaction = allInteractions.stream()
                        .filter(di -> di.matches(drug1, drug2))
                        .findFirst();

                interaction.ifPresent(foundInteractions::add);
            }
        }

        log.info("Found interactions: " + foundInteractions);

        return new DrugInteractionResponse(foundInteractions);
    }
}