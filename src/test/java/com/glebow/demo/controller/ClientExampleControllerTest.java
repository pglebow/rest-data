/**
 * 
 */
package com.glebow.demo.controller;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.glebow.demo.domain.User;
import com.glebow.demo.domain.Users;
import com.glebow.demo.repository.UserRepository;
import com.glebow.demo.util.Util;
import com.google.common.io.Resources;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pglebow
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class ClientExampleControllerTest {

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
    public void testClientEndpoint() {
        try {
            Users users = restTemplate.getForObject("/users/search/findByLastName?lastName={lastName}", Users.class,
                    "u0L");
            Assert.assertNotNull(users);
            Assert.assertNotNull(users.getContent());
            Assert.assertEquals(1, users.getContent().size());

            Iterator<User> iterator = users.getContent().iterator();
            User u = iterator.next();
            Assert.assertNotNull(u);
            Assert.assertNotNull(u.getId());

            ResponseEntity<User> userFromClient = restTemplate.getForEntity("/client?id={id}&version={version}", User.class, u.getId(), 0);
            Assert.assertNotNull(userFromClient);
            Assert.assertTrue(userFromClient.getStatusCode().is2xxSuccessful());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }
    }

}
