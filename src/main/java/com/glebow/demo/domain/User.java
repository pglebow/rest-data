/**
 * 
 */
package com.glebow.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pglebow
 *
 */
@Data
@Document(collection="users")
@NoArgsConstructor
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
