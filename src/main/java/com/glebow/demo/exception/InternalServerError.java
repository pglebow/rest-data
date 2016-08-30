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
@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Error") 
public class InternalServerError extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -8168918676524593646L;

}
