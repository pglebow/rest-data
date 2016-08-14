/**
 * 
 */
package com.glebow.demo.config;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * @author pglebow
 *
 */
public class CloudConfig extends AbstractCloudConfig {
	
	@Bean
	public MongoDbFactory documentMongoDbFactory() {
		return connectionFactory().mongoDbFactory();
	}
}
