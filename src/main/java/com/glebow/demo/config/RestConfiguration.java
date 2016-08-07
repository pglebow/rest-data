/**
 * 
 */
package com.glebow.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glebow.demo.domain.User;

/**
 * @author pglebow
 *
 */
@Configuration
public class RestConfiguration extends RepositoryRestMvcConfiguration {

	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(User.class);
	}
	
	/**
	 * Added due to a bug in Spring Boot 1.4
	 * 
	 * {@link} https://github.com/spring-projects/spring-boot/issues/6529
	 */
	@Override
	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		return super.objectMapper();
	}

}
