package com.glebow.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
@EnableCaching
public class RestDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDataApplication.class, args);
	}

	@Bean(name = "cacheManager")
	public CacheManager getCacheManager() {
		ConcurrentMapCacheManager m = new ConcurrentMapCacheManager("client");
		return m;
	}

	@Bean
	public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
		Resource sourceData = new ClassPathResource("users.json");
		Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
		factory.setResources(new Resource[] { sourceData });
		return factory;
	}
}
