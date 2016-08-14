package com.glebow.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
@EnableCaching
@EnableAutoConfiguration(exclude = { EmbeddedMongoAutoConfiguration.class })
public class RestDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDataApplication.class, args);
	}

	@Bean(name = "cacheManager")
	public CacheManager getCacheManager() {
		ConcurrentMapCacheManager m = new ConcurrentMapCacheManager("client");
		return m;
	}
}
