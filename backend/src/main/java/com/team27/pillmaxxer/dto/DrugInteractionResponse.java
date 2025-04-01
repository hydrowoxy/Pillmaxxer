package com.team27.pillmaxxer.dto;

import lombok.Data;

import java.util.List;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class DrugInteractionResponse {

    private List<DrugInteraction> interactionsList;

    @AllArgsConstructor
    @Data
    public static class DrugInteraction {
        private String drugA;
        private String drugB;
        private String level;

        public boolean matches(String d1, String d2) {
            return (drugA.equalsIgnoreCase(d1) && drugB.equalsIgnoreCase(d2)) ||
                    (drugA.equalsIgnoreCase(d2) && drugB.equalsIgnoreCase(d1));
        }
    }
}
