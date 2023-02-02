package com.saurs.talktome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class TalktomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalktomeApplication.class, args);
	}

}
