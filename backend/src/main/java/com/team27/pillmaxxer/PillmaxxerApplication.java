package com.team27.pillmaxxer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.java.Log;

@SpringBootApplication
@Log
public class PillmaxxerApplication {
	public static void main(String[] args) {
		log.info("initiating pillmaxxer...");
		SpringApplication.run(PillmaxxerApplication.class, args);
		log.info("we are ready to pillmaxx!");
	}
}
