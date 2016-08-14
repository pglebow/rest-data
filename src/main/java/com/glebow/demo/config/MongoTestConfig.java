/**
 * 
 */
package com.glebow.demo.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.flapdoodle.embed.process.config.IRuntimeConfig;

/**
 * @author pglebow
 *
 */
@Configuration
@Profile("test")
public class MongoTestConfig extends EmbeddedMongoAutoConfiguration {

	public MongoTestConfig(MongoProperties properties, EmbeddedMongoProperties embeddedProperties,
			ApplicationContext context, IRuntimeConfig runtimeConfig) {
		super(properties, embeddedProperties, context, runtimeConfig);		
	}

}
