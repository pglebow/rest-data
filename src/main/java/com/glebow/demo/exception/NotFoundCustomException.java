/**
 * 
 */
package com.glebow.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author pglebow
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Not found") 
public class NotFoundCustomException extends RuntimeException {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4909836354479340722L;

}
