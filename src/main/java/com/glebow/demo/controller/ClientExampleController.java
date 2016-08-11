/**
 * 
 */
package com.glebow.demo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.glebow.demo.domain.User;

import lombok.extern.slf4j.Slf4j;

/**
 * This class demonstrates how to use ETags provided by an endpoint.
 * <P>
 * Note that this is a client of the other API in the project and is only intended to demonstrate how to use ETags.
 * 
 * @author pglebow
 *
 */
@Controller
@Slf4j
@RequestMapping(path = "/client")
public class ClientExampleController {

    private RestTemplate template = new RestTemplate();

    @Value("#{cacheManager.getCache('client')}")
    private Cache cache;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getUser(@RequestParam String id, @RequestParam String version) throws URISyntaxException {
        ResponseEntity<?> retVal = null;
        String eTag = null;

        URI userEndpointUri = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("users").replaceQuery(null)
                .pathSegment(id).build().toUri();

        Optional<ResponseEntity<User>> oreu = getUserFromEndpoint(userEndpointUri, version);
        if (oreu.isPresent()) {
            ResponseEntity<?> r = oreu.get();
            retVal = r;
            eTag = r.getHeaders().getETag() != null ? r.getHeaders().getETag() : null;

            log.info("Request for " + id + " and version " + version + " returned status code "
                    + r.getStatusCode().toString());

            if (HttpStatus.OK.equals(r.getStatusCode())) {
                // Normal response; remove the existing version and cache the new one
                cache.put(id, retVal);
                log.info("Caching " + id + " (version = " + eTag + ")");
            } else if (HttpStatus.NOT_MODIFIED.equals(r.getStatusCode())) {
                // This entity has not been modified so check cache and if not present, load it
                retVal = cache.get(id, ResponseEntity.class);
                if (retVal == null) {
                    log.info("Unable to find " + id + " in the cache; reloading from the endpoint");
                    try {
                        Optional<ResponseEntity<User>> reLoaded = getUserFromEndpoint(userEndpointUri, null);
                        if (reLoaded.isPresent()) {
                            // Found it
                            retVal = reLoaded.get();
                            cache.put(id, retVal);
                            log.info("Added id " + id + " to the cache (content=" + retVal.toString() + ")");
                        }
                    } catch (Exception e) {
                        log.error("Error getting " + id + " and version " + version + ": " + e.getMessage(), e);
                    }

                }
            }
        }

        if (retVal == null) {
            retVal = ResponseEntity.notFound().build();
        }

        return retVal;
    }

    /**
     * Retrieves a ResponseEntity from the endpoint using the id and version provided
     * 
     * @param id
     * @param version,
     *            optional
     * @return
     * @throws URISyntaxException
     */
    private Optional<ResponseEntity<User>> getUserFromEndpoint(final URI uri, final String version)
            throws URISyntaxException {
        Optional<ResponseEntity<User>> retVal = Optional.empty();

        if (uri != null) {
            RequestEntity<Void> entity = null;
            if (version != null) {
                entity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).ifNoneMatch("\"" + version + "\"").build();
            } else {
                entity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
            }

            ResponseEntity<User> r = template.exchange(uri, HttpMethod.GET, entity, User.class);
            retVal = Optional.ofNullable(r);
        }

        return retVal;
    }

}
