package no.auke.drone.domain.rss;

import java.util.Date;

import com.sun.syndication.feed.synd.SyndEntryImpl;

public class CustomSyndEntryImpl extends SyndEntryImpl {
    private static final long serialVersionUID = 3475130630650810469L;
    private Date publishedDate;

    @Override
    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public Date getPublishedDate() {
        return publishedDate;
    }

}
