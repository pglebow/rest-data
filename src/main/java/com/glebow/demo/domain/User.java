/**
 * 
 */
package com.glebow.demo.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pglebow
 *
 */
@Data
@Document(collection = "users")
@NoArgsConstructor
public class User {

	@Id
	private String id;

	@Version
	private Long version;

	@Indexed
	private String firstName;

	@Indexed
	private String lastName;

	@Indexed(unique = true)
	private String email;

	@LastModifiedDate
	private Date lastModifiedDate;
}
