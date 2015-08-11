package no.auke.drone.application.impl;

import java.util.Date;

import no.auke.drone.application.TrackerFactory;
import no.auke.drone.application.TrackerUpdater;
import no.auke.drone.domain.Device;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.SimpleTracker;
import no.auke.drone.domain.Tracker;
import no.auke.drone.entity.TrackerDB;

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
    public Tracker create(Tracker tracker) {
        if(tracker == null) {
            return null;
        }
        tracker.setTrackerUpdater(trackerUpdater);
        tracker.setCreateDate(new Date());
        tracker.setModifiedDate(new Date());
        return tracker;
    }

    @Override
    public Tracker from(Device device) {
        if(device == null) return null;
        return create(Tracker.TrackerType.REAL.toString(), device.getDeviceID(), device.getDescription(), device.getDescription(), Tracker.TrackerType.REAL ,null,  new MapPoint(device.getLastValidLatitude(),device.getLastValidLongitude()), device.getImeiNumber(), device.getSimPhoneNumber(), "", true);
    }

    @Override
    public Tracker from(TrackerDB trackerDB) {
        if(trackerDB == null) return null;
        return create(trackerDB.getLayer(),trackerDB.getId(),trackerDB.getName(), trackerDB.getDescription(), null, trackerDB.getOwner(), null, null, null, trackerDB.getContactInfo(), trackerDB.isStoredTrips());
    }

    @Override
    public Tracker create(String trackerLayer, String id, String name, String description, Tracker.TrackerType droneType,
                          String owner, MapPoint location, String imei, String simPhone, String contactInfo, boolean storeTrip) {
        // sanitize input
        if(location == null) {
            location = new MapPoint();
        }

    	Tracker tracker = new SimpleTracker();
        tracker.setTrackerUpdater(trackerUpdater);
        tracker.setId(id);
        tracker.setActive(true);
        tracker.setLayerId(trackerLayer);
        tracker.setName(name);
        tracker.setOwner(owner);
        tracker.setSimPhone(simPhone);
        tracker.setImeiNumber(imei);
        tracker.setCreateDate(new Date());
        tracker.setModifiedDate(new Date());
        tracker.setTrackerType(droneType);
        tracker.setDescription(description);
        tracker.setContactInfo(contactInfo);
        tracker.setStoredTrips(storeTrip);
        tracker.setCurrentPosition(location);
        tracker.getPositions().add(tracker.getCurrentPosition());
        return tracker;
    }
}
