package com.coding2themax.career.game.careergamebatchservice;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class CareerGameBatchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerGameBatchServiceApplication.class, args);
	}

}
