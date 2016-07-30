/**
 * 
 */
package com.glebow.demo.service;

import java.util.List;

import com.glebow.demo.domain.User;

/**
 * @author pglebow
 *
 */
public interface UserService {

	List<User> findAll();
	
	User findByEmail(String email);
	
	User createUser(String firstName, String lastName, String email) throws IllegalArgumentException;
	
	void deleteUser(String id);
}
