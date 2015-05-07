package no.auke.rss.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import no.auke.drone.dto.AukeResponse;
import no.auke.drone.services.RSSFeedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/rss")
@Component
@Produces(MediaType.APPLICATION_XHTML_XML)
public class RSSFeedResource {
    
    
    @Autowired
    private RSSFeedServices feedServices;
    
    @GET
    @Path("/{filename}")
    public Response getContent(@PathParam(value = "filename") String filename) throws FileNotFoundException {
        StreamingOutput stream = null;
        final InputStream in = getClass().getResourceAsStream("/rss/" + filename);
        stream = new StreamingOutput() {
            public void write(OutputStream out) throws IOException, WebApplicationException {
                try {
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    while ((read = in.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        return Response.ok(stream).build();
    }
    
    @GET
    @Path("/make-rss")
    public AukeResponse generateRSSFeed(){
//        String copyright = "Copyright by Auke Team";
//        String title = "Eclipse and Java Information";
//        String description = "Eclipse and Java Information";
//        String language = "en";
//        String link = "http://localhost:8080";
//        Calendar cal = new GregorianCalendar();
//        Date creationDate = cal.getTime();
//        SimpleDateFormat date_format = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
//        String pubdate = date_format.format(creationDate);
//        Feed rssFeeder = new Feed(title, link, description, language, copyright, pubdate);
//
//        // now add one example entry
//        FeedMessage feed = new FeedMessage();
//        feed.setTitle("RSSFeed");
//        feed.setDescription("This is a description");
//        feed.setAuthor("thaihuynh@gmail.com");
//        feed.setGuid("http://localhost/drone/RSSFeed/article.html");
//        feed.setLink("http://localhost/drone/RSSFeed/article.html");
//        rssFeeder.getMessages().add(feed);
        
        // Add logic get The last 10 "objects" registered
        // The 10 longest flight times registered
        Feed rssFeeder = null;
        boolean isOk = feedServices.generateRSSFeed(rssFeeder);
        return new AukeResponse(isOk);
    }

}
