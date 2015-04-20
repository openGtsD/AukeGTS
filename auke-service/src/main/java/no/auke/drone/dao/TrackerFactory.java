package no.auke.drone.dao;

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
    Tracker create(String trackerLayer, String id, String name, double altitude, double speed, long time, Tracker.TrackerType droneType, Person flyer, boolean hasCamera, MapPoint location, String imei, String simPhone);
}
