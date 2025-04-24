package com.hubstaff.integration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class IntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegrationApplication.class, args);
	}

	@Bean
	public ModelMapper getMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return  new RestTemplate();
	}
}
