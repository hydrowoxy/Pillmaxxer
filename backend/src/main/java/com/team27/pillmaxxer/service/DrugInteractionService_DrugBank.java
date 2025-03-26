package com.team27.pillmaxxer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * Service for fetching drug interaction warnings from the RxNorm API.
 */
@Service
public class DrugInteractionService_DrugBank {

    // Base URL for the FDA's RxNorm API
    private static final String RXNORM_BASE_URL = "https://rxnav.nlm.nih.gov/REST";

    // RestTemplate for making HTTP requests to the RxNorm API
    private final RestTemplate restTemplate;

    public DrugInteractionService_DrugBank() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Fetches the RxCUI (RxNorm Concept Unique Identifier) for a given drug.
     * Example: If the user enters "Ibuprofen", this method retrieves the RxCUI "5640".
     *
     * @param drugName The name of the drug.
     * @return The RxCUI (a unique identifier for the drug) or null if not found.
     */
    public String getRxCUI(String drugName) {
        try {
            String url = RXNORM_BASE_URL + "/rxcui.json?name=" + drugName;
            System.out.println("Fetching RxCUI for drug: " + drugName);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getBody() == null || !response.getBody().containsKey("idGroup")) {
                return null;
            }

            Map<String, Object> idGroup = (Map<String, Object>) response.getBody().get("idGroup");
            List<String> rxcuiList = (List<String>) idGroup.get("rxnormId");

            if (rxcuiList != null && !rxcuiList.isEmpty()) {
                System.out.println("Found RxCUI: " + rxcuiList.get(0));
                return rxcuiList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error fetching RxCUI: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fetches a list of dangerous drug and food interactions for the given drug.
     *
     * @param drug The name of the drug (e.g., "Ibuprofen").
     * @return A list of interaction warnings or "No known dangerous interactions" if none exist.
     */
    public List<String> getDangerousInteractions(String drug) {
        String rxcui = getRxCUI(drug);
        if (rxcui == null) {
            return List.of("Drug not found in RxNorm database.");
        }

        try {
// Corrected API endpoint for drug interactions
            String interactionUrl = RXNORM_BASE_URL + "/interaction/interaction.json?rxcui=" + rxcui;
            System.out.println("Fetching interactions from: " + interactionUrl);

            ResponseEntity<Map> response = restTemplate.getForEntity(interactionUrl, Map.class);
            Map<String, Object> interactionData = response.getBody();

            if (interactionData == null || !interactionData.containsKey("fullInteractionTypeGroup")) {
                return List.of("No known dangerous interactions.");
            }

            List<Map<String, Object>> interactionGroups = (List<Map<String, Object>>) interactionData.get("fullInteractionTypeGroup");
            if (interactionGroups.isEmpty()) {
                return List.of("No known dangerous interactions.");
            }

            List<String> interactionWarnings = new ArrayList<>();
            for (Map<String, Object> group : interactionGroups) {
                List<Map<String, Object>> interactions = (List<Map<String, Object>>) group.get("fullInteractionType");
                for (Map<String, Object> interaction : interactions) {
                    List<Map<String, Object>> interactionPairs = (List<Map<String, Object>>) interaction.get("interactionPair");
                    for (Map<String, Object> pair : interactionPairs) {
                        String interactionDesc = (String) pair.get("description");
                        interactionWarnings.add("" + interactionDesc);
                    }
                }
            }

            return interactionWarnings.isEmpty() ? List.of("ℹ️ No known dangerous interactions.") : interactionWarnings;

        } catch (Exception e) {
            System.out.println("Error fetching interactions: " + e.getMessage());
            return List.of("Error retrieving interaction data.");
        }
    }
}