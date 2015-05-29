package no.auke.drone.domain.rss;

import java.util.Date;

import com.sun.syndication.feed.synd.SyndFeedImpl;

public class CustomSyndFeed extends SyndFeedImpl {

    private static final long serialVersionUID = -6381817521247178601L;
    protected Date publishedDate;
    protected String language;
    protected String copyright;

    @Override
    public Date getPublishedDate() {
        return publishedDate;
    }

    @Override
    public void setPublishedDate(final Date publishedDate) {
        this.publishedDate = new Date(publishedDate.getTime());
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String getCopyright() {
        return this.copyright;
    }

    @Override
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
