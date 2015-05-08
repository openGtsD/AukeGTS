package no.auke.rss.resource;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import no.auke.drone.domain.Tracker;
import no.auke.drone.dto.AukeResponse;
import no.auke.drone.services.RSSFeedServices;
import no.auke.drone.services.TrackerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/rss")
@Component
public class RSSFeedResource {

    @Autowired
    private RSSFeedServices feedServices;
    @Autowired
    private TrackerService trackerService;

    @GET
    @Path("/{filename}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getContent(@PathParam(value = "filename") String filename) throws FileNotFoundException {
        StreamingOutput stream = feedServices.getContent(filename);
        return Response.ok(stream).build();
    }

    @GET
    @Path("/make-rss")
    @Produces(MediaType.APPLICATION_JSON)
    public AukeResponse generateRSSFeed() {
        Collection<Tracker> trackers = trackerService.getActiveTrackers();
        Feed rssFeeder = feedServices.buildFeed(trackers);
        List<Feed> feeds = new ArrayList<Feed>();
        boolean isOk = feedServices.generateRSSFeed(rssFeeder, "articles.rss");
        if (isOk) {
            feeds.add(rssFeeder);
        }
        return new AukeResponse(!feeds.isEmpty(), feeds);
    }

}
