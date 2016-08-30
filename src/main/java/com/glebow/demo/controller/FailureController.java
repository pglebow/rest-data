/**
 * 
 */
package com.glebow.demo.controller;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
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
    
    private AtomicInteger count = new AtomicInteger();

    @RequestMapping("/notFound")
    public ResponseEntity<?> notFound() {
        throw new NotFoundCustomException();
    }
    
    @RequestMapping("/count")
    public ResponseEntity<String> count() {
        ResponseEntity<String> retVal = null;
        
        int currentValue = count.incrementAndGet();
        
        if ( currentValue % 3 == 0) {
            throw new NotFoundCustomException();
        } else {
            retVal = new ResponseEntity<String>("Count is " + count, HttpStatus.OK);
        }
        
        return retVal;
    }
    
}
