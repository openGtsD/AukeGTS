package no.auke.drone.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import no.auke.drone.domain.CustomSyndFeed;
import no.auke.drone.domain.Tracker;
import no.auke.drone.services.RSSFeedServices;
import no.auke.drone.services.TrackerService;
import no.auke.drone.utils.YmlPropertiesPersister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;

@Service
public class RSSFeedServiceImpl implements RSSFeedServices {

    private SimpleDateFormat DATE_PARSER = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private YmlPropertiesPersister propertiesPersister;

    @Override
    public StreamingOutput makeRss(String type) {
        SyndFeed feed = buildFeed(type);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            SyndFeedOutput output = new SyndFeedOutput();
            String result = output.outputString(feed);
            stream.write(result.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponse(stream);
    }

    private StreamingOutput toResponse(final ByteArrayOutputStream data) {
        StreamingOutput stream = null;
        stream = new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                output.write(data.toByteArray());
            }
        };
        return stream;
    }

    private SyndFeed buildFeed(String type) {
    	
    	// LHA: ok here we have diffrent type og feed
    	// create sub metods
    	
    	
    	
    	
        Collection<Tracker> trackers = trackerService.getActiveTrackers();
        SyndFeed feed = new CustomSyndFeed();
        feed.setFeedType(propertiesPersister.getPropertyByKey("rss.type"));
        feed.setTitle(propertiesPersister.getPropertyByKey("rss.title"));
        feed.setDescription(propertiesPersister.getPropertyByKey("rss.description"));
        feed.setCopyright(propertiesPersister.getPropertyByKey("rss.copyright"));
        feed.setLanguage(propertiesPersister.getPropertyByKey("rss.language"));
        feed.setLink(propertiesPersister.getPropertyByKey("server.url") + type);
        try {
            feed.setPublishedDate(DATE_PARSER.parse(DATE_PARSER.format(new GregorianCalendar().getTime())));
        } catch (ParseException e) {
            feed.setPublishedDate(new GregorianCalendar().getTime());
        }
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        for (Tracker tracker : trackers) {
            SyndEntryImpl entry = new SyndEntryImpl();
            entry.setTitle(tracker.getName());
            SyndContentImpl description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(tracker.getName());
            entry.setDescription(description);
            entry.setAuthor(tracker.getFlyer() != null ? tracker.getFlyer().getEmail() : "auketeam@gmail.com"
                    + "(Auke Team) ");
            entry.setLink(propertiesPersister.getPropertyByKey("server.domain"));
            entry.setUri(propertiesPersister.getPropertyByKey("server.domain") + "/" + UUID.randomUUID().toString());
            entries.add(entry);
        }
        feed.setEntries(entries);
        return feed;
    }
}
