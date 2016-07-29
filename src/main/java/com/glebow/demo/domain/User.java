/**
 * 
 */
package com.glebow.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

/**
 * @author pglebow
 *
 */
@Data
@Document(collection="users")
@Builder
public class User {

	@Id
	private String id;
	
	//@Version
	private long version;
	
	@Indexed
	private String firstName;
	
	@Indexed
	private String lastName;
	
	@Indexed(unique=true)
	private String email;
	
}
