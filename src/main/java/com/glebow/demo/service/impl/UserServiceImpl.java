/**
 * 
 */
package com.glebow.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glebow.demo.domain.User;
import com.glebow.demo.repository.UserRepository;
import com.glebow.demo.service.UserService;

/**
 * @author pglebow
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	/* (non-Javadoc)
	 * @see com.glebow.demo.service.UserService#findAll()
	 */
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see com.glebow.demo.service.UserService#findByEmail(java.lang.String)
	 */
	@Override
	public User findByEmail(String email) {
		User retVal = null;
		
		nonNullNotBlank(email, "email");
		
		retVal = userRepository.findByEmail(email);
		
		return retVal;
	}

	@Override
	public User createUser(String firstName, String lastName, String email) throws IllegalArgumentException {
		User retVal = null;
		
		nonNullNotBlank(firstName, "firstName");
		nonNullNotBlank(lastName, "lastName");
		nonNullNotBlank(email, "email");
		
		User newUser = User.builder().firstName(firstName).lastName(lastName).email(email).build();
		
		retVal = userRepository.save(newUser);
		
		return retVal;
	}
	
	/**
	 * Checks to see that s is non-null and not empty
	 * @param s
	 * @param name
	 * @throws IllegalArgumentException if s is null or empty
	 */
	private void nonNullNotBlank(final String s, final String name) throws IllegalArgumentException {
		if ( s == null || s.isEmpty() ) {
			throw new IllegalArgumentException(name + " must be non-null and not empty");
		}
	}

}
