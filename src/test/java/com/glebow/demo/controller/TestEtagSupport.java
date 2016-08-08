/**
 * 
 */
package com.glebow.demo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.glebow.demo.domain.User;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pglebow
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class TestEtagSupport {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value("${local.server.port}")
	int port;

	@Test
	public void test() throws URISyntaxException {
		ResponseEntity<User> r1 = this.restTemplate.getForEntity("/users/57a7a634de62000b5080f8e1", User.class);
		Assert.assertTrue(r1.hasBody());
		Assert.assertEquals(HttpStatus.OK, r1.getStatusCode());
		Assert.assertNotNull(r1.getHeaders().getETag(), "ETag must not be null");

		HttpHeaders headers = new HttpHeaders();
		headers.setIfNoneMatch("\"0\"");

		ResponseEntity<User> r2 = this.restTemplate.exchange("/users/57a7a634de62000b5080f8e1", HttpMethod.GET,
				new HttpEntity<>(headers), User.class);
		Assert.assertFalse(r2.hasBody());
		Assert.assertEquals(HttpStatus.NOT_MODIFIED, r2.getStatusCode());
	}

	@Test
	public void testEtagsWithTraverson() throws URISyntaxException {
		URI uri = new URI("http://localhost:" + port + "/users");
		log.info(uri.toString());

		Traverson t = new Traverson(uri, MediaTypes.HAL_JSON);

		PagedResources<Resource<User>> resources = t.follow(Link.REL_FIRST)
				.toObject(new TypeReferences.PagedResourcesType<Resource<User>>() {
				});
		Assert.assertNotNull(resources);

		Iterator<Resource<User>> iterator = resources.iterator();
		Resource<User> res1 = iterator.next();

		Link l = res1.getId();
		ResponseEntity<User> r1 = this.restTemplate.getForEntity(l.getHref(), User.class);
		Assert.assertTrue(r1.hasBody());
		Assert.assertEquals(HttpStatus.OK, r1.getStatusCode());
		Assert.assertNotNull(r1.getHeaders().getETag(), "ETag must not be null");

		HttpHeaders headers = new HttpHeaders();
		headers.setIfNoneMatch("\"0\"");

		ResponseEntity<User> r2 = this.restTemplate.exchange(l.getHref(), HttpMethod.GET, new HttpEntity<>(headers),
				User.class);
		Assert.assertFalse(r2.hasBody());
		Assert.assertEquals(HttpStatus.NOT_MODIFIED, r2.getStatusCode());
	}

	@Test
	public void testResourcesWithTraverson() throws URISyntaxException {
		URI uri = new URI("http://localhost:" + port + "/users");
		log.info(uri.toString());

		Traverson t = new Traverson(uri, MediaTypes.HAL_JSON);
		traverse(t, Link.REL_FIRST, null);
	}
	
	@Test
	public void testCacheByEtags() throws URISyntaxException {
		Set<User> users = Sets.newHashSet();
		HashMap<String, String> cache = new HashMap<String, String>();
		Traverson t = new Traverson(new URI("http://localhost:" + port + "/users"), MediaTypes.HAL_JSON);
		traverse(t, Link.REL_FIRST, users);
		Assert.assertFalse(users.isEmpty());
		
		// Populate the cache
		for (User u : users ) {
			Assert.assertNotNull(u.getId());
			ResponseEntity<User> r1 = this.restTemplate.getForEntity("/users/" + u.getId(), User.class);
			Assert.assertTrue(r1.hasBody());
			Assert.assertEquals(HttpStatus.OK, r1.getStatusCode());
			String eTag = r1.getHeaders().getETag();
			Assert.assertNotNull(eTag, "ETag must not be null");
			cache.put(u.getId(), eTag);			
		}
		
		for (Map.Entry<String, String> e : cache.entrySet()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setIfNoneMatch("\"" + e.getValue() + "\"");
			ResponseEntity<User> r2 = this.restTemplate.exchange(new URI("http://localhost:" + port + "/users" + e.getKey()), HttpMethod.GET, new HttpEntity<>(headers),
					User.class);
			Assert.assertFalse(r2.hasBody());
			Assert.assertEquals(HttpStatus.NOT_MODIFIED, r2.getStatusCode());
		}
	}

	/**
	 * Recursively traverse through all next links until the end is reached
	 * 
	 * @param t
	 * @param linkRel
	 */
	private void traverse(final Traverson t, String linkRel, Set<User> users) {
		log.info("Traversing " + t.toString() + " via " + linkRel);
		PagedResources<Resource<User>> resources = t.follow(linkRel)
				.toObject(new TypeReferences.PagedResourcesType<Resource<User>>() {
				});
		Assert.assertNotNull(resources);
		for (Resource<User> resource : resources) {
			final User customer = resource.getContent();			
			log.info(customer.toString());
			if ( users != null ) {
				users.add(customer);
			}
		}
		if (resources.hasLink(Link.REL_NEXT)) {
			traverse(t, resources.getNextLink().getRel(), users);
		}
	}

}
