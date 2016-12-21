package com.glebow.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { EmbeddedMongoAutoConfiguration.class })
public class RestDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestDataApplication.class, args);
    }
}
