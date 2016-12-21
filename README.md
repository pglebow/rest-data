# Table of Contents
1. [rest-data](#rest-data)
2. [ETags](#etags)
3. [Getting Started](#getting-started)
4. [Things to Try](#things-to-try)
5. [OAUTH2](#oauth2)
6. [References](#references)
 

# rest-data
![Build Status](https://travis-ci.org/pglebow/rest-data.svg?branch=master)

This is a small vignette that demonstrates how to expose entities using Spring Data REST that use entity tags (ETags) for caching.

## ETags
Entity Tags (ETags) are primarily used to implement caching of resources across the Internet.  From [RFC 7232](https://tools.ietf.org/html/rfc7232):

> Conditional requests are HTTP requests [RFC7231] that include one or
   more header fields indicating a precondition to be tested before
   applying the method semantics to the target resource.  This document
   defines the HTTP/1.1 conditional request mechanisms in terms of the
   architecture, syntax notation, and conformance criteria defined in
   [RFC7230].
   
>   Conditional GET requests are the most efficient mechanism for HTTP
   cache updates [RFC7234].  Conditionals can also be applied to
   state-changing methods, such as PUT and DELETE, to prevent the "lost
   update" problem: one client accidentally overwriting the work of
   another client that has been acting in parallel.

>   Conditional request preconditions are based on the state of the
   target resource as a whole (its current value set) or the state as
   observed in a previously obtained representation (one value in that
   set).  A resource might have multiple current representations, each
   with its own observable state.  The conditional request mechanisms
   assume that the mapping of requests to a "selected representation"
   (Section 3 of [RFC7231]) will be consistent over time if the server
   intends to take advantage of conditionals.  Regardless, if the
   mapping is inconsistent and the server is unable to select the
   appropriate representation, then no harm will result when the
   precondition evaluates to false.

The image below depicts the requests, headers and actions that are taken when ETags are used.

![](images/etags.png?raw=true "Image credit: http://thespringthing.blogspot.com/2015/06/etags-and-browser-cache.html")

## Getting Started

### Tests
To begin, open and run the test [TestEtagSupport](src/test/java/com/glebow/demo/controller/TestEtagSupport.java).

This project uses the [Embedded Mongo Database](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) and the first time that you run it, it will download the appropriate binary.  After that, things will be much faster.

Once you've run the first test, [ClientExampleControllerTest](src/test/java/com/glebow/demo/controller/ClientExampleControllerTest.java) demonstrates how to use ETags if you're calling a REST endpoint that supports them.

### Run as a Spring Boot Application
To run this from the command line, do

> `gradle bootRun`

For now, this is setup to use the embedded MongoDB.  To use your own MongoDB, change this line in [build.gradle](build.gradle)

> compile('de.flapdoodle.embed:de.flapdoodle.embed.mongo:1.50.3')

to

> testCompile('de.flapdoodle.embed:de.flapdoodle.embed.mongo:1.50.3')

You will also need to provide any configuration necessary to interact with your DB.  See the Spring Data REST documentation for instructions.

## Things to Try
### List all Users

> [http://localhost:8080/users](http://localhost:8080/users)

### Add a User
POST this content with the header `"Content-type" : "application/json"` 

> { "firstName" : "Phil", "lastName" : "Glebow", "email" : "p@g.com" }

to this endpoint

> [http://localhost:8080/users](http://localhost:8080/users)


### Search for a user
Search for the user that you've added by following this link

> [http://localhost:8080/users/search/findByLastName?lastName=Glebow](http://localhost:8080/users/search/findByLastName?lastName=Glebow)

### Get the user you found
Follow the self/href link, e.g., 

> [http://localhost:8080/users/57aa431d06271c4a6c763069](http://localhost:8080/users/57aa431d06271c4a6c763069)

to load the user.  The status code will `200` and the user will be in the body of the response.  If you inspect the headers, you'll find an ETag header.  If you add this header

> If-None-Match: "0"

and reload the entity, you'll get a status code of `304/Not modified` and the body of the response will be empty.  This indicates to the caller that the entity has not changed.  The network will not have to transport the body which will improve the performance characteristics of the system.

### Run the [ClientExampleControllerTest](src/test/java/com/glebow/demo/controller/ClientExampleControllerTest.java)
Look at the messages in the log; they describe the actions taken when attempting to retrieve a user.

## OAUTH2
OAUTH2 is a specification that for authentication and authorizations.  See the references for a in-depth discussion of the framework.  
This project uses Spring Security to perform a very basic authentication and authorization flow using Facebook and Github.  
I've deployed two "applications" to demonstrate this - one on Facebook and one on Github.
The demo simply displays what data is made avaialble once a user authenticates via either platform.
The internal SSO version is being developed but we're having some difficulty with Ping Federate as of this writing.

## References
| Description | Link |
| ---- | ---- |
| Spring Data REST Reference Page | http://projects.spring.io/spring-data-rest/ |
| RFC 7232: Conditional Requests (ETags) | https://tools.ietf.org/html/rfc7232 |
| de.flapdoodle.embed.mongo Embedded MongoDB | https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo |
| ETags and browser cache | http://thespringthing.blogspot.com/2015/06/etags-and-browser-cache.html |
| An Introduction to OAUTH2 | https://www.digitalocean.com/community/tutorials/an-introduction-to-oauth-2 |
| OAuth | https://oauth.net/2 |
| Spring Boot and OAuth2 | https://spring.io/guides/tutorials/spring-boot-oauth2/ |