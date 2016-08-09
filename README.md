# rest-data
This is a small vignette that demonstrates how to expose entities using Spring Data REST that use entity tags (ETags) for caching.

## Getting Started

### Tests
To begin, open and run the test [TestEtagSupport](src/test/java/com/glebow/demo/controller/TestEtagSupport.java).

This project uses the [Embedded Mongo Database](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) and the first time that you run it, it will download the appropriate binary.  After that, things will be much faster.

### Run as a Spring Boot Application
To run this from the command line, do

> `gradle bootRun`

For now, this is setup to use the embedded MongoDB.  To use your own MongoDB, change this line in [build.gradle](build.gradle)

> compile('de.flapdoodle.embed:de.flapdoodle.embed.mongo:1.50.3')

to

> testCompile('de.flapdoodle.embed:de.flapdoodle.embed.mongo:1.50.3')

You will also need to provide any configuration necessary to interact with your DB.  See the Spring Data REST documentation for instructions.

## ETags
!(images/etags.png)

## References
| Description | Link |
| ---- | ---- |
| Spring Data REST Reference Page | http://projects.spring.io/spring-data-rest/ |
| RFC 7232: Conditional Requests (ETags) | https://tools.ietf.org/html/rfc7232 |
| de.flapdoodle.embed.mongo Embedded MongoDB | https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo |