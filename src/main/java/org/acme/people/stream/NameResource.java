package org.acme.people.stream;

import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;

/**
 * A simple resource retrieving the in-memory "my-data-stream" and sending the items as server-sent events.
 */
@Path("/names")
public class NameResource {

    // Injects the my-data-stream stream using the @Channel qualifier
    @Inject
    @Channel("my-data-stream") Publisher<String> names; 

    @GET
    @Path("/stream")
    // Indicates that the content is sent using Server Sent Events
    @Produces(MediaType.SERVER_SENT_EVENTS)
    // Indicates that the data contained within the server sent events is of type text/plain
    @SseElementType("text/plain") 
    // Returns the stream (Reactive Stream)
    public Publisher<String> stream() { 
        return names;
    }
}