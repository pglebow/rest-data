/**
 * 
 */
package com.glebow.demo.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.glebow.demo.domain.User;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pglebow
 *
 */
@Controller
@Slf4j
@RequestMapping(path = "/client")
public class ClientExampleController {

	private RestTemplate template = new RestTemplate();

	private String uriString = "http://localhost:8080/users/";

	@Value("#{cacheManager.getCache('client')}")
	private Cache cache;

	@GetMapping
	@ResponseBody
	public User getUser(@RequestParam String id, @RequestParam String version) throws URISyntaxException {
		User retVal = null;

		URI uri = getUri(id);
		RequestEntity<Void> entity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).ifNoneMatch(version). build();
		
		ResponseEntity<User> r = template.exchange(uri, HttpMethod.GET, entity, User.class);

		if (r.hasBody()) {
			retVal = r.getBody();
			cache.putIfAbsent(getKey(retVal), retVal);
		}

		return retVal;
	}

	private Object getKey(final User user) {
		Object retVal = null;

		if (user != null && user.getId() != null && user.getVersion() != null) {
			retVal = new String(user.getId() + "_" + user.getVersion());
		}

		return retVal;
	}

	private URI getUri(String id) throws URISyntaxException {
		return new URI(uriString + id);
	}
}
