package org.acme.people.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.security.identity.SecurityIdentity;

// Note that we do not use any @RolesAllowed.
// It looks like an ordinary endpoint. 
// Keycloak (the server) is the one enforcing access here, not Quarkus directly.
@Path("/secured") 
public class KeycloakResource {

    // The SecurityIdentity is a generic object produced by the Keycloak extension that we can use 
    // to obtain information about the security principals and attributes embedded in the request.
    @Inject
    SecurityIdentity identity; 

    @GET
    @Path("/confidential") 
    @Produces(MediaType.TEXT_PLAIN)
    public String confidential() {
        return ("confidential access for: " + identity.getPrincipal().getName() +
          " with attributes:" + identity.getAttributes());
    }
}