package no.auke.drone.dao;

import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Person;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface TrackerFactory {
    Tracker createDrone(String id, String name);

    Tracker createDrone(String id, String name, MapPoint location);

    Tracker createDrone(String id, String name, double altitude, double speed, long time, String droneType, Person flyer, boolean hasCamera, MapPoint location);
}
