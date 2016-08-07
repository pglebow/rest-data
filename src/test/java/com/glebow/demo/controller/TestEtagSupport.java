/**
 * 
 */
package com.glebow.demo.controller;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.glebow.demo.domain.User;

/**
 * @author pglebow
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestEtagSupport {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void test() throws URISyntaxException {
		ResponseEntity<User> r1 = this.restTemplate.getForEntity("/users/57a7a634de62000b5080f8e1", User.class);
		Assert.assertTrue(r1.hasBody());
		Assert.assertEquals(HttpStatus.OK, r1.getStatusCode());
		Assert.assertNotNull(r1.getHeaders().getETag(), "ETag must not be null");

		HttpHeaders headers  = new HttpHeaders();
		headers.setIfNoneMatch("\"0\"");
		
		ResponseEntity<User> r2 = this.restTemplate.exchange("/users/57a7a634de62000b5080f8e1", HttpMethod.GET, new HttpEntity<>(headers), User.class);
		Assert.assertFalse(r2.hasBody());
		Assert.assertEquals(HttpStatus.NOT_MODIFIED, r2.getStatusCode());
	}

}
