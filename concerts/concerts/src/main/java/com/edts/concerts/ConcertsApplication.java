package com.edts.concerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ConcertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertsApplication.class, args);
	}
}