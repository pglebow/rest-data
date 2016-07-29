/**
 * 
 */
package com.glebow.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.glebow.demo.domain.User;
import com.glebow.demo.service.UserService;

/**
 * @author pglebow
 *
 */
@Controller
@RequestMapping(path="/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(path="/all", method=RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> retVal = userService.findAll();
		
		return new ResponseEntity<List<User>>(retVal, HttpStatus.OK);
	}
	
	@RequestMapping(path="/add", method=RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody User newUser) {
		User u = userService.createUser(newUser.getFirstName(), newUser.getLastName(), newUser.getEmail());
		return new ResponseEntity<User>(u, HttpStatus.CREATED); 
	}
}
