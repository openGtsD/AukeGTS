package no.auke.drone.domain.rss;

import com.sun.syndication.feed.atom.Link;

public class SyndicationLink {
    private final Link link = new Link();

    public Link getLink() {
        return link;
    }

    public SyndicationLink withRel(String rel) {
        link.setRel(rel);
        return this;
    }

    public SyndicationLink withType(String type) {
        link.setType(type);
        return this;
    }

    public SyndicationLink withHref(String href) {
        link.setHref(href);
        return this;
    }

    public SyndicationLink withTitle(String title) {
        link.setTitle(title);
        return this;
    }

    public SyndicationLink withLength(final long length) {
        link.setLength(length);
        return this;
    }
}