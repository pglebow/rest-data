/**
 * 
 */
package com.glebow.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.glebow.demo.domain.User;

/**
 * @author pglebow
 *
 */
public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);
	List<User> findByLastName(String lastName);
}
