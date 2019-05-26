package com.assignment.shareapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShareAppWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareAppWebApplication.class, args);
	}

}
