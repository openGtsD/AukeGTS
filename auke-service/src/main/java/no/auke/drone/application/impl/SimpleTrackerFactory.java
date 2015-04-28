package no.auke.drone.application.impl;

import java.util.Date;

import no.auke.drone.application.TrackerUpdater;
import no.auke.drone.application.TrackerFactory;
import no.auke.drone.domain.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huyduong on 3/25/2015.
 */
@Service
public class SimpleTrackerFactory implements TrackerFactory {
    private static final Logger logger = LoggerFactory.getLogger(SimpleTrackerFactory.class);

    @Autowired
    private TrackerUpdater trackerUpdater;

    @Override
    public Tracker create(String id, String name) {
        return create("REAL",id, name, new MapPoint(0,0,0,0,0));
    }
    
    @Override
    public Tracker create(String trackerLayer, String id, String name) {
        return create(trackerLayer,id, name, new MapPoint(0,0,0,0,0));
    }

    @Override
    public Tracker create(String trackerLayer, String id, String name, MapPoint location) {
    	return create(trackerLayer, id, name, 0, 0, 0, null, null, false, location, "" , "");
    }

    @Override
    public Tracker from(Device device) {
        return create(Tracker.TrackerType.REAL.toString(), device.getDeviceID(), device.getDescription());
    }

    @Override
    public Tracker create(String trackerLayer, String id, String name, double altitude, double speed, long time, Tracker.TrackerType droneType,
                          Person flyer, boolean hasCamera, MapPoint location, String imei, String simPhone) {
    	Tracker tracker = new SimpleTracker();
        tracker.setTrackerUpdater(trackerUpdater);
        tracker.setId(id);
        tracker.setLayerId(trackerLayer);
        tracker.setName(name);
        tracker.setAltitude(altitude);
        tracker.setSimPhone(simPhone);
        tracker.setCreateDate(new Date());
        tracker.setModifiedDate(new Date());
        tracker.setSpeed(speed);
        tracker.setTrackerType(droneType);
        tracker.setFlyer(flyer);
        tracker.setUsedCamera(hasCamera);
        tracker.setCurrentPosition(location);
        tracker.getPositions().add(tracker.getCurrentPosition());
        return tracker;
    }
}
