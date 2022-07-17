package com.example.cluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RabbitDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitDemoApplication.class, args);
	}
}
