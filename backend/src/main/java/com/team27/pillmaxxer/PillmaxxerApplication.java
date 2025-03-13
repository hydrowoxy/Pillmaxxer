package com.team27.pillmaxxer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PillmaxxerApplication {

	public static void main(String[] args) {
		System.out.println("Starting Pillmaxxer application...");
		SpringApplication.run(PillmaxxerApplication.class, args);
		System.out.println("Pillmaxxer application started successfully!");
	}

}
