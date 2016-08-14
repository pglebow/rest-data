/**
 * 
 */
package com.glebow.demo.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.glebow.demo.domain.User;
import com.glebow.demo.repository.UserRepository;
import com.glebow.demo.util.Util;
import com.google.common.io.Resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pglebow
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
@ActiveProfiles("test")
public class TestEtagSupport {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value("${local.server.port}")
	int port;

	@Autowired
	private UserRepository r;

	@Before
	public void insertData() throws IOException {
		if (r.count() == 0) {
			r.save(Util.parseJsonIntoUsers(Resources.getResource("users.json")));
		}
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
	public void testResourcesWithTraverson() throws URISyntaxException, MalformedURLException {
		URI uri = new URI("http://localhost:" + port + "/users");
		log.info(uri.toString());

		Traverson t = new Traverson(uri, MediaTypes.HAL_JSON);
		traverse(t, Link.REL_FIRST, null);
	}

	@Test
	public void testCacheByEtags() throws URISyntaxException, MalformedURLException {
		HashMap<String, CacheEntry<String, String, User>> cache = new HashMap<String, CacheEntry<String, String, User>>();
		Traverson t = new Traverson(new URI("http://localhost:" + port + "/users"), MediaTypes.HAL_JSON);
		traverse(t, Link.REL_FIRST, cache);
		Assert.assertFalse(cache.isEmpty());

		// Populate the cache by calling each ID and saving the Etag to our
		// cache
		for (Map.Entry<String, CacheEntry<String, String, User>> u : cache.entrySet()) {
			Assert.assertNotNull(u.getKey());
			CacheEntry<String, String, User> cacheEntry = u.getValue();

			ResponseEntity<User> r1 = this.restTemplate.getForEntity("/users/" + u.getKey(), User.class);
			Assert.assertTrue(r1.hasBody());
			Assert.assertEquals(HttpStatus.OK, r1.getStatusCode());
			String eTag = r1.getHeaders().getETag();
			Assert.assertNotNull(eTag, "ETag must not be null");
			cacheEntry.setETag(eTag);
		}

		// Re-retrieve each user object, checking to see that we get 304/not
		// modified for each of them
		for (Map.Entry<String, CacheEntry<String, String, User>> e : cache.entrySet()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setIfNoneMatch(e.getValue().getETag());
			ResponseEntity<User> r2 = this.restTemplate.exchange(
					new URI("http://localhost:" + port + "/users/" + e.getKey()), HttpMethod.GET,
					new HttpEntity<>(headers), User.class);
			Assert.assertFalse(r2.hasBody());
			Assert.assertEquals(HttpStatus.NOT_MODIFIED, r2.getStatusCode());
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class CacheEntry<KeyType, ETagType, ValueType> {
		KeyType key;
		ETagType eTag;
		ValueType value;
	}

	/**
	 * Recursively traverse through all next links until the end is reached
	 * 
	 * @param t
	 * @param linkRel
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	private void traverse(final Traverson t, String linkRel, HashMap<String, CacheEntry<String, String, User>> cache)
			throws URISyntaxException, MalformedURLException {
		log.info("Traversing " + t.toString() + " via " + linkRel);
		PagedResources<Resource<User>> resources = t.follow(linkRel)
				.toObject(new TypeReferences.PagedResourcesType<Resource<User>>() {
				});
		Assert.assertNotNull(resources);
		for (Resource<User> resource : resources) {
			final User customer = resource.getContent();
			log.info(customer.toString());
			if (cache != null) {
				URI uri = new URI(resource.getId().getHref());
				URL url = uri.toURL();
				File f = new File(url.getFile());
				String id = f.getName();
				cache.put(id, new CacheEntry<String, String, User>(id, null, customer));
			}
		}
		if (resources.hasLink(Link.REL_NEXT)) {
			traverse(t, resources.getNextLink().getRel(), cache);
		}
	}

}
