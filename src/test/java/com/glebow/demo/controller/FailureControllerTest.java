package com.glebow.demo.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
@ActiveProfiles("test")
public class FailureControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testFailure() {
        ResponseEntity<?> r = restTemplate.getForEntity("/fail/notFound", Object.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
    }
    
    @Test
    public void testInternalServerError() {
        ResponseEntity<?> r = restTemplate.getForEntity("/fail/internalError", Object.class);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, r.getStatusCode());
    }
    
    @Test
    public void testCount() {
        for ( int i = 1; i <= 10; i++ ) {
            ResponseEntity<String> r = restTemplate.getForEntity("/fail/count", String.class);
            if ( i % 3 == 0 ) {
                Assert.assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());                
            } else {
                Assert.assertEquals(HttpStatus.OK, r.getStatusCode());
                log.info(r.getBody());
            }            
        }
    }

}
