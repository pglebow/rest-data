/**
 * 
 */
package com.glebow.demo.config;

import javax.sql.DataSource;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;

/**
 * @author pglebow
 *
 */
public class CloudConfig extends AbstractCloudConfig {
	
	@Bean
	public DataSource dataSourceFactory() {
		return connectionFactory().dataSource();
	}
}
