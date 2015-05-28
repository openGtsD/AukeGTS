package no.auke.rss.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import no.auke.drone.services.RSSFeedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/rss")
@Component
@Produces(MediaType.APPLICATION_XML)
public class RSSFeedResource {

    @Autowired
    private RSSFeedServices feedServices;

    @GET
    @Path("/{layerId}/{type}")
    public Response getRSS(@PathParam("type") String type, @PathParam("layerId") String layer) {
        StreamingOutput stream = feedServices.makeRss(type, layer);
        return Response.ok(stream).build();
    }

}
