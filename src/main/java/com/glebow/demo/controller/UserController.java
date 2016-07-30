/**
 * 
 */
package com.glebow.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.glebow.demo.domain.User;
import com.glebow.demo.service.UserService;

/**
 * @author pglebow
 *
 */
@Controller
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> retVal = userService.findAll();

		return new ResponseEntity<List<User>>(retVal, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody User newUser) {
		User u = userService.createUser(newUser.getFirstName(), newUser.getLastName(), newUser.getEmail());
		return new ResponseEntity<User>(u, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="{id}")
	public ResponseEntity<User> deleteUser(@PathVariable String id) {
		userService.deleteUser(id);
		return new ResponseEntity<User>(HttpStatus.OK);
	}

	@RequestMapping(params="emailAddress", method = RequestMethod.GET)
	public ResponseEntity<User> getByEmail(@RequestParam(value="emailAddress") String emailAddress) {
		ResponseEntity<User> retVal = null;

		User u = userService.findByEmail(emailAddress);
		
		if ( u == null ) {
			retVal = new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		} else {
			retVal = new ResponseEntity<User>(u, HttpStatus.FOUND);
		}

		return retVal;
	}

}
