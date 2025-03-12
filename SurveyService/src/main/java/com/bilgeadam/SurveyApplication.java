package com.bilgeadam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SurveyApplication {
	public static void main(String[] args) {
		SpringApplication.run(SurveyApplication.class, args);
	}
}