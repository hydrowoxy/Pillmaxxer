package com.team27.pillmaxxer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PillmaxxerApplication { 
	public static void main(String[] args) {
		System.out.println("initiating pillmaxxer...");
		SpringApplication.run(PillmaxxerApplication.class, args);
		System.out.println("we are ready to pillmax!");
	}
}
