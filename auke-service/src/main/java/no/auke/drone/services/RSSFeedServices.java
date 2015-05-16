package no.auke.drone.services;

import javax.ws.rs.core.StreamingOutput;

public interface RSSFeedServices {
    public StreamingOutput makeRss(String type);
}