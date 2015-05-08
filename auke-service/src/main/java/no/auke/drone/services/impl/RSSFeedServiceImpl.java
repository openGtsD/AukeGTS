package no.auke.drone.services.impl;

import java.awt.SystemColor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import no.auke.drone.domain.Tracker;
import no.auke.drone.services.RSSFeedServices;
import no.auke.drone.utils.YmlPropertiesPersister;
import no.auke.rss.resource.Feed;
import no.auke.rss.resource.FeedMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RSSFeedServiceImpl implements RSSFeedServices {

    @Autowired
    private YmlPropertiesPersister propertiesPersister;

    @Override
    public boolean generateRSSFeed(Feed rssfeed, String fileName) {
        // create a XMLOutputFactory
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        // create XMLEventWriter
        XMLEventWriter eventWriter;
        try {
            eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(buildOutputFile(fileName)));

            // create a EventFactory

            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");

            // create and write Start Tag

            StartDocument startDocument = eventFactory.createStartDocument();

            eventWriter.add(startDocument);

            // create open tag
            eventWriter.add(end);

            StartElement rssStart = eventFactory.createStartElement("", "", "rss");
            eventWriter.add(rssStart);
            eventWriter.add(eventFactory.createAttribute("version", "2.0"));
            eventWriter.add(end);

            eventWriter.add(eventFactory.createStartElement("", "", "channel"));
            eventWriter.add(end);

            // Write the different nodes

            createNode(eventWriter, "title", rssfeed.getTitle());

            createNode(eventWriter, "link", rssfeed.getLink() + fileName);

            createNode(eventWriter, "description", rssfeed.getDescription());

            createNode(eventWriter, "language", rssfeed.getLanguage());

            createNode(eventWriter, "copyright", rssfeed.getCopyright());

            createNode(eventWriter, "pubDate", rssfeed.getPubDate());
            
            for (FeedMessage entry : rssfeed.getMessages()) {
                eventWriter.add(eventFactory.createStartElement("", "", "item"));
                eventWriter.add(end);
                createNode(eventWriter, "title", entry.getTitle());
                createNode(eventWriter, "description", entry.getDescription());
                createNode(eventWriter, "link", entry.getLink());
                createNode(eventWriter, "guid", entry.getGuid());
                createNode(eventWriter, "author", entry.getAuthor());
                eventWriter.add(end);
                eventWriter.add(eventFactory.createEndElement("", "", "item"));
                eventWriter.add(end);
            }
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndElement("", "", "channel"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndElement("", "", "rss"));

            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
           
            String url = propertiesPersister.getPropertyByKey("server.url");
            url += fileName;
            rssfeed.setUrl(url);
            
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (XMLStreamException e) {
            return false;
        }

    }

    private void createNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    private File buildOutputFile(String fileExtention) {
        File outputDir = new File(propertiesPersister.getPropertyByKey("rss-generation.output-dir"));
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        return new File(outputDir, fileExtention);
    }

    @Override
    public StreamingOutput getContent(String filename) {
        StreamingOutput stream = null;
        try {
            final InputStream in = new FileInputStream(buildOutputFile(filename));
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
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        return stream;
    }

    @Override
    public Feed buildFeed(Collection<Tracker> trackers) {
        
        // THAi - remove hard code later
        String copyright = "Copyright by Auke Team";
        String title = "RSS feed by Auke Team";
        String description = "RSS feed by Auke Team";
        String language = "en";
        Calendar cal = new GregorianCalendar();
        Date creationDate = cal.getTime();
        SimpleDateFormat date_format = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
        String pubdate = date_format.format(creationDate);
        Feed rssFeeder = new Feed(title, propertiesPersister.getPropertyByKey("server.url"), description, language, copyright, pubdate);
        for (Tracker tracker : trackers) {
            FeedMessage feed = new FeedMessage();
            feed.setTitle(tracker.getName());
            feed.setDescription(tracker.getName());
            feed.setAuthor(tracker.getFlyer() != null ? tracker.getFlyer().getEmail() : "auketeam@gmail.com" + "(Auke Team) ");
            feed.setLink(propertiesPersister.getPropertyByKey("server.domain"));
            feed.setGuid(propertiesPersister.getPropertyByKey("server.domain") + "/" + System.currentTimeMillis());
            rssFeeder.getMessages().add(feed);
        }
        return rssFeeder;
    }
}
