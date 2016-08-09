/**
 * 
 */
package com.glebow.demo.util;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import com.glebow.demo.domain.User;
import com.google.common.io.Resources;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pglebow
 *
 */
@Slf4j
public class TestUtil {

	/**
	 * Test method for {@link com.glebow.demo.util.Util#parseJson(org.springframework.core.io.Resource)}.
	 */
	@Test
	public void testParseJson() {
		try {
			URL r = Resources.getResource("users.json");
			List<Document> l = Util.parseJson(r);
			Assert.assertNotNull(l);
			for ( Document d : l ) {
				log.info(d.toJson());
			}
		} catch (IOException e) {
			log.error(e.getMessage());
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testUser() {
		try {
			URL r = Resources.getResource("users.json");
			Set<User> users = Util.parseJsonIntoUsers(r);
			Assert.assertNotNull(users);
			users.forEach(user -> log.info(user.toString()));
		} catch (IOException e) {
			log.error(e.getMessage());
			Assert.fail(e.getMessage());
		}
		
	}

}
