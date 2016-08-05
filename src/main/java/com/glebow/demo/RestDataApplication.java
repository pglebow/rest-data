package com.glebow.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableCaching
public class RestDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDataApplication.class, args);
	}
	
	@Bean(name="cacheManager")
	public CacheManager getCacheManager() {
		ConcurrentMapCacheManager m = new ConcurrentMapCacheManager("client");
		return m;
	}
}
