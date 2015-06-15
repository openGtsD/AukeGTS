package no.auke.drone.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.rss.CustomModuleImpl;

import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;

public class RSSUtil {
    private static SimpleDateFormat DATE_PARSER = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z",
            Locale.US);

    @SuppressWarnings("unchecked")
    public static void setAtomInfo(String link, String rel, String appOrXml, SyndFeed feed) {
        CustomModuleImpl module = new CustomModuleImpl();
        module.setRel(rel);
        module.setHref(link);
        module.setType(appOrXml);
        feed.getModules().add(module);
    }

    public static void setItemsInfo(Tracker tracker, SyndEntryImpl entry, String domain) {
//        SyndEnclosureImpl enclosures = new SyndEnclosureImpl();
//        
//        enclosures.setLength(20);
//        enclosures.setType("img/gif");
//        enclosures.setUrl(domain + "/auke-js/ui/images/flight.gif");
//        entry.setEnclosures(Arrays.asList(enclosures));

        entry.setTitle(tracker.getName());
        SyndContentImpl description = new SyndContentImpl();
        description.setType("text/plain");
        description.setValue(tracker.getName() + tracker.getId());

        entry.setDescription(description);
        entry.setAuthor(tracker.getFlyer() != null ? tracker.getFlyer().getEmail() : "admin");
        entry.setLink(domain);
        entry.setUri(domain + "/" + UUID.randomUUID().toString());
        entry.setPublishedDate(tracker.getCreateDate());
    }

    public static void setChannelInfo(SyndFeed feed, YmlPropertiesPersister propertiesPersister) {
        feed.setFeedType(propertiesPersister.getPropertyByKey("rss.type"));
        feed.setTitle(propertiesPersister.getPropertyByKey("rss.title"));
        feed.setDescription(propertiesPersister.getPropertyByKey("rss.description"));
        feed.setCopyright(propertiesPersister.getPropertyByKey("rss.copyright"));
        feed.setLanguage(propertiesPersister.getPropertyByKey("rss.language"));
        feed.setLink(propertiesPersister.getPropertyByKey("server.domain"));
        try {
            feed.setPublishedDate(DATE_PARSER.parse(DATE_PARSER.format(new GregorianCalendar().getTime())));
        } catch (ParseException e) {
            feed.setPublishedDate(new GregorianCalendar().getTime());
        }
    }
}
