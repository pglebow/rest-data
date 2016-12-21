/**
 * 
 */
package com.glebow.demo.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pglebow
 *
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = "cacheManager")
    public CacheManager getCacheManager() {
        ConcurrentMapCacheManager m = new ConcurrentMapCacheManager("client");
        return m;
    }
}
