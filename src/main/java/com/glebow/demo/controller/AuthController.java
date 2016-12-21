/**
 * 
 */
package com.glebow.demo.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glebow.demo.util.Util;

/**
 * Note:  Make sure to use @RestController here instead of @Controller!
 * @author pglebow
 *
 */
@RestController
public class AuthController {
    
    @RequestMapping("/auth")
    public Principal user(Principal principal) {
        Principal retVal = principal;

        Util.logPrincipalDetails(retVal);

        return retVal;
    }
}
