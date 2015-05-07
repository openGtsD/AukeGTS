package no.auke.drone.services;

import java.util.Collection;

import javax.ws.rs.core.StreamingOutput;

import no.auke.drone.domain.Tracker;
import no.auke.rss.resource.Feed;

public interface RSSFeedServices {
    public boolean generateRSSFeed(Feed feed, String fileName);

    public StreamingOutput getContent(String filename);
    
    public Feed buildFeed(Collection<Tracker> trackers);
}