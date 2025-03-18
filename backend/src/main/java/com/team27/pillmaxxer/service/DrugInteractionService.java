package com.team27.pillmaxxer.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class DrugInteractionService {

    // Dummy database for drug interactions (to be replaced by real data)
    private static final Map<String, String> drugInteractions = new HashMap<>();

    static {
        //Bad Interactions
        drugInteractions.put("Warfarin-Aspirin", "Dangerous: Increases bleeding risk.");
        drugInteractions.put("Ibuprofen-Lisinopril", "Warning: Can reduce kidney function.");
        drugInteractions.put("Statins-Grapefruit", "Avoid: Grapefruit increases statin toxicity.");
        drugInteractions.put("Antibiotics-Alcohol", "Avoid: Reduces effectiveness and causes nausea.");

        // Good Interactions
        drugInteractions.put("Vitamin D-Calcium", "Good: Enhances calcium absorption.");
        drugInteractions.put("Iron-Vitamin C", "Good: Vitamin C helps iron absorption.");
        drugInteractions.put("Probiotics-Fiber", "Good: Supports gut health.");
    }

    /**
     * Checks for drug interactions between two drugs.
     * @param drug1 First drug name.
     * @param drug2 Second drug name.
     * @return Interaction description (good or bad) if found, otherwise "No known interaction."
     */
    public String checkInteraction(String drug1, String drug2) {
        String key1 = drug1 + "-" + drug2;
        String key2 = drug2 + "-" + drug1;

        if (drugInteractions.containsKey(key1)) {
            return drugInteractions.get(key1);
        } else if (drugInteractions.containsKey(key2)) {
            return drugInteractions.get(key2);
        } else {
            return "No known interaction.";
        }
    }
}
