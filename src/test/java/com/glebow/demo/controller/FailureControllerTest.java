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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FailureControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testFailure() {
        ResponseEntity<?> r = restTemplate.getForEntity("/fail/notFound", Object.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
    }
}
