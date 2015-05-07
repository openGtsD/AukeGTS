package no.auke.drone.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

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
    public boolean generateRSSFeed(Feed rssfeed) {
        // create a XMLOutputFactory
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        // create XMLEventWriter
        XMLEventWriter eventWriter;
        try {
            eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(buildOutputFile("articles.rss")));

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

            createNode(eventWriter, "link", rssfeed.getLink());

            createNode(eventWriter, "description", rssfeed.getDescription());

            createNode(eventWriter, "language", rssfeed.getLanguage());

            createNode(eventWriter, "copyright", rssfeed.getCopyright());

            createNode(eventWriter, "pubdate", rssfeed.getPubDate());

            for (FeedMessage entry : rssfeed.getMessages()) {
                eventWriter.add(eventFactory.createStartElement("", "", "item"));
                eventWriter.add(end);
                createNode(eventWriter, "title", entry.getTitle());
                createNode(eventWriter, "description", entry.getDescription());
                createNode(eventWriter, "link", entry.getLink());
                createNode(eventWriter, "author", entry.getAuthor());
                createNode(eventWriter, "guid", entry.getGuid());
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
}
