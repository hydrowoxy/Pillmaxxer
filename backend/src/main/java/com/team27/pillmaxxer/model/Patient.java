package com.team27.pillmaxxer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String patientId; // Firestore document ID
    private String userId; // Links to Firebase Auth UID
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<String> deviceTokens; // For FCM notifications
}
