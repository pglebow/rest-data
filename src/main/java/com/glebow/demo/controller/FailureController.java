/**
 * 
 */
package com.glebow.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.glebow.demo.exception.NotFoundCustomException;

/**
 * This controller throws exceptions periodically which is useful when demonstrating how to leverage an APM tool.
 * @author pglebow
 *
 */
@Controller
@RequestMapping(path = "/fail")
public class FailureController {

    @RequestMapping("/notFound")
    public ResponseEntity<?> notFound() {
        throw new NotFoundCustomException();
    }
    
}
