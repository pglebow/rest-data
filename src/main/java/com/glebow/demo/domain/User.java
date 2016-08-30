/**
 * 
 */
package com.glebow.demo.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.springframework.data.annotation.LastModifiedDate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @author pglebow
 *
 */
@Data
@Entity(name="User")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(AccessLevel.NONE)
	private Long id;

	@Version
	private Long version;

	private String firstName;

	private String lastName;

	private String email;

	@LastModifiedDate
	private Date lastModifiedDate;
}
