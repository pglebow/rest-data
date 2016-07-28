/**
 * 
 */
package com.glebow.demo.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.glebow.demo.service.UserService;

/**
 * @author pglebow
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceImplTest {

	@Autowired
	private UserService s;
	
	@Test(expected=IllegalArgumentException.class)
	public void testException() {
		s.createUser(null, null, null);
	}


}
