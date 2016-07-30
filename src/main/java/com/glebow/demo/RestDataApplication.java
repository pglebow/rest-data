package com.glebow.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class RestDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDataApplication.class, args);
	}
}
