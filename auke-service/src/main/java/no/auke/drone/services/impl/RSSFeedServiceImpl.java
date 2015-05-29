package no.auke.drone.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.rss.CustomSyndEntryImpl;
import no.auke.drone.domain.rss.CustomSyndFeed;
import no.auke.drone.services.RSSFeedServices;
import no.auke.drone.services.TrackerService;
import no.auke.drone.utils.RSSUtil;
import no.auke.drone.utils.YmlPropertiesPersister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;

@Service
public class RSSFeedServiceImpl implements RSSFeedServices {
    @Autowired
    private TrackerService trackerService;

    @Autowired
    private YmlPropertiesPersister propertiesPersister;

    @Override
    public StreamingOutput makeRss(String type, String layer) {
        SyndFeed feed = buildFeed(type, layer);
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

    private SyndFeed buildFeed(String type, String layer) {
        // LHA: ok here we have diffrent type og feed create sub metods
        Collection<Tracker> trackers = getTrackersByType(type, layer);
        SyndFeed feed = new CustomSyndFeed();
        
        RSSUtil.setChannelInfo(feed, propertiesPersister);
        
        String link = propertiesPersister.getPropertyByKey("server.url") + layer + "/" + type;
        String rel = propertiesPersister.getPropertyByKey("rss.rel");
        String appOrXml = propertiesPersister.getPropertyByKey("rss.appOrXml");
        RSSUtil.setAtomInfo(link, rel, appOrXml, feed);
        
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        for (Tracker tracker : trackers) {
            SyndEntryImpl entry = new CustomSyndEntryImpl();
            String domain = propertiesPersister.getPropertyByKey("server.domain");
            RSSUtil.setItemsInfo(tracker, entry, domain);
            entries.add(entry);
        }
        feed.setEntries(entries);
        return feed;
    }

    private Collection<Tracker> getTrackersByType(String type, String layer) {
        Collection<Tracker> trackers = Collections.emptyList();
        if (type.equalsIgnoreCase(propertiesPersister.getPropertyByKey("tracker.registered"))) {
            trackers = trackerService.getLatestRegisteredTrackers(layer);
        } else if (type.equalsIgnoreCase(propertiesPersister.getPropertyByKey("tracker.longest"))) {
            trackers = trackerService.getLongestFlightTrackers(layer);
        }
        return trackers;
    }
}
