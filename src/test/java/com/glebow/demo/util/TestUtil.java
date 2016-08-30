/**
 * 
 */
package com.glebow.demo.util;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

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
//    @Test
//    public void testParseJson() {
//        try {
//            URL r = Resources.getResource("users.json");
//            List<Document> l = Util.parseJson(r);
//            Assert.assertNotNull(l);
//            for (Document d : l) {
//                log.info(d.toJson());
//            }
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            Assert.fail(e.getMessage());
//        }
//    }

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

    @Test
    public void messingWithServletUriComponentsBuilder() {
        UriComponents u1 = ServletUriComponentsBuilder
                .fromUriString("http://localhost:8080/client?id={id}&version={version}").port(1234).host("newHost")
                .replaceQueryParam("id", "id123").replaceQueryParam("version", 0).replacePath("home").build();
        log.info(u1.toString());
        UriComponents u2 = ServletUriComponentsBuilder
                .fromUriString("http://localhost:8080/client?id={id}&version={version}").replacePath("users").replaceQuery(null).pathSegment("123").build();
        log.info(u2.toString());
    }

}
