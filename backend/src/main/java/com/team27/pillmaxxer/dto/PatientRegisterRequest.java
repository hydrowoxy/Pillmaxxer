package com.team27.pillmaxxer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegisterRequest {
    String email;
    String password;
    String firstName;
    String lastName;
    String phoneNumber;
    List<String> deviceTokens;
}
