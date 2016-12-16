package com.glebow.demo;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

import com.glebow.demo.util.Util;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableCaching
@EnableAutoConfiguration(exclude = { EmbeddedMongoAutoConfiguration.class })
@EnableOAuth2Client
@EnableAuthorizationServer
@RestController
@EnableGlobalMethodSecurity
@Slf4j
public class RestDataApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    public static void main(String[] args) {
        SpringApplication.run(RestDataApplication.class, args);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class).antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**").permitAll().anyRequest().authenticated().and().logout()
                .logoutSuccessUrl("/").permitAll().and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    // http.antMatcher("/**")
    // .authorizeRequests()
    // .antMatchers("/", "/login**", "/webjars/**").permitAll()
    // .anyRequest().authenticated() .and().exceptionHandling()
    // .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"));
    // }

    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(facebook(), "/login/facebook"));
        filters.add(ssoFilter(github(), "/login/github"));
        filter.setFilters(filters);
        return filter;
    }

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(template);
        filter.setTokenServices(
                new UserInfoTokenServices(client.getResource().getUserInfoUri(), client.getClient().getClientId()));
        return filter;
    }

    @Bean
    @ConfigurationProperties("github")
    public ClientResources github() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
        return new ClientResources();
    }

    @RequestMapping("/auth")
    public Principal user(Principal principal) {
        Principal retVal = principal;
        
        Util.logPrincipalDetails(retVal);
        
        return retVal;
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean(name = "cacheManager")
    public CacheManager getCacheManager() {
        ConcurrentMapCacheManager m = new ConcurrentMapCacheManager("client");
        return m;
    }
}
