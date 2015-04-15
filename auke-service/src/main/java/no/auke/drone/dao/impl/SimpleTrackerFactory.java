package no.auke.drone.dao.impl;

import no.auke.drone.dao.TrackerFactory;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Person;
import no.auke.drone.domain.SimpleTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/25/2015.
 */
public class SimpleTrackerFactory implements TrackerFactory {
    private static final Logger logger = LoggerFactory.getLogger(SimpleTrackerFactory.class);

    @Override
    public Tracker create(String id, String name) {
        return create("DEFAULT",id, name, new MapPoint(0,0,0,0,0));
    }
    
    @Override
    public Tracker create(String trackerLayer, String id, String name) {
        return create(trackerLayer,id, name, new MapPoint(0,0,0,0,0));
    }

    @Override
    public Tracker create(String trackerLayer, String id, String name, MapPoint location) {
        
    	Tracker tracker = new SimpleTracker();
        tracker.setId(id);
        tracker.setLayerid(trackerLayer);
        tracker.setName(name);
        tracker.setCurrentPosition(location);
        tracker.getPositions().add(tracker.getCurrentPosition());
        logger.info("created tracker: " + tracker.toString());
        
        return tracker;
    }

    @Override
    public Tracker create(String trackerLayer, String id, String name, double altitude, double speed, long time, Tracker.TrackerType droneType,
                          Person flyer, boolean hasCamera, MapPoint location) {
        
    	Tracker tracker = new SimpleTracker();
        tracker.setId(id);
        tracker.setLayerid(trackerLayer);
        tracker.setName(name);
        tracker.setAltitude(altitude);
        tracker.setSpeed(speed);
        tracker.setTrackerType(droneType);
        tracker.setFlyer(flyer);
        tracker.setUsedCamera(hasCamera);
        tracker.setCurrentPosition(location);
        tracker.getPositions().add(tracker.getCurrentPosition());
        return tracker;
    
    }

}
