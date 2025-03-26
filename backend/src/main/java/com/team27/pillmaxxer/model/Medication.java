package com.team27.pillmaxxer.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medication {
    private String id;
    private String name;

    /*
     * I am not sure if the prescription that the user inputs will give us this
     * information
     * or if we will have to get it from an api. I will leave it here for now.
     */
    // private String description;
    // private List<String> activeIngredients;
    // private String dosageForms; // tablet, capsule, liquid, etc.
    // private String manufacturer;
}