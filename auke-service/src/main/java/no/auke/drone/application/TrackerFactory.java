package no.auke.drone.application;

import no.auke.drone.domain.Device;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Person;
import no.auke.drone.entity.TrackerDB;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface TrackerFactory {
    Tracker create(Tracker tracker);
	Tracker create(String id, String name);
    Tracker create(String trackerLayer, String id, String name);
    Tracker create(String trackerLayer, String id, String name, MapPoint location);
    Tracker create(String trackerLayer, String id, String name, String description, Tracker.TrackerType droneType, String owner, MapPoint location, String imei, String simPhone);
    Tracker from(Device device);
    Tracker from(TrackerDB trackerDB);

}
