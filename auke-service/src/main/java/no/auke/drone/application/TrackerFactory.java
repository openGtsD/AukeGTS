package no.auke.drone.application;

import no.auke.drone.domain.Device;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Person;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface TrackerFactory {
	Tracker create(String id, String name);
    Tracker create(String trackerLayer, String id, String name);
    Tracker create(String trackerLayer, String id, String name, MapPoint location);
    Tracker create(String trackerLayer, String id, String name, Tracker.TrackerType droneType, Person flyer, MapPoint location, String imei, String simPhone);
    Tracker from(Device device);
}
