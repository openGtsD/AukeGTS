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
    	return create(trackerLayer, id, name, Tracker.TrackerType.fromValue(trackerLayer),null, location, "", "");
    }

    @Override
    public Tracker from(Device device) {
        if(device == null) return null;
        return create(Tracker.TrackerType.REAL.toString(), device.getDeviceID(), device.getDescription(), Tracker.TrackerType.REAL ,null,  new MapPoint(), device.getImeiNumber(), device.getSimPhoneNumber());
    }

    @Override
    public Tracker create(String trackerLayer, String id, String name, Tracker.TrackerType droneType,
                          Person flyer, MapPoint location, String imei, String simPhone) {
    	Tracker tracker = new SimpleTracker();
        tracker.setTrackerUpdater(trackerUpdater);
        tracker.setId(id);
        tracker.setActive(true);
        tracker.setLayerId(trackerLayer);
        tracker.setName(name);
        tracker.setSimPhone(simPhone);
        tracker.setImeiNumber(imei);
        tracker.setCreateDate(new Date());
        tracker.setModifiedDate(new Date());
        tracker.setTrackerType(droneType);
        tracker.setFlyer(flyer);
        tracker.setCurrentPosition(location);
        tracker.getPositions().add(tracker.getCurrentPosition());
        return tracker;
    }
}
