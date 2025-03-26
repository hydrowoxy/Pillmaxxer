package com.team27.pillmaxxer.service;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class DrugInteractionService_DDInter {

    // Store unique interactions
    private final Set<String> seenPairs = new HashSet<>();
    private final List<Interaction> allInteractions = new ArrayList<>();

    // Directory of your CSV files (in /resources/)
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

    // Represents one interaction record
    private static class Interaction {
        String drugA;
        String drugB;
        String level;

        Interaction(String drugA, String drugB, String level) {
            this.drugA = drugA.toLowerCase();
            this.drugB = drugB.toLowerCase();
            this.level = level;
        }

        boolean matches(String d1, String d2) {
            return (drugA.equalsIgnoreCase(d1) && drugB.equalsIgnoreCase(d2)) ||
                    (drugA.equalsIgnoreCase(d2) && drugB.equalsIgnoreCase(d1));
        }
    }

    @PostConstruct
    public void loadAllCSVs() {
        for (String fileName : fileNames) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName)),
                    StandardCharsets.UTF_8))) {

                String line;
                reader.readLine(); // Skip header
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",", -1);
                    if (tokens.length >= 5) {
                        String a = tokens[1].trim().toLowerCase();
                        String b = tokens[3].trim().toLowerCase();
                        String level = tokens[4].trim();

                        // Normalize pair key to avoid duplicates
                        String pairKey = String.join("::", a.compareTo(b) <= 0 ? a : b, a.compareTo(b) <= 0 ? b : a);

                        if (!seenPairs.contains(pairKey)) {
                            seenPairs.add(pairKey);
                            allInteractions.add(new Interaction(a, b, level));
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading file: " + fileName);
                e.printStackTrace();
            }
        }
    }

    public Optional<String> getInteractionLevel(String drug1, String drug2) {
        String d1 = drug1.toLowerCase();
        String d2 = drug2.toLowerCase();
        return allInteractions.stream()
                .filter(i -> i.matches(d1, d2))
                .map(i -> i.level)
                .findFirst();
    }
}
