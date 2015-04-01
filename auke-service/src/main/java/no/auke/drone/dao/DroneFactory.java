package no.auke.drone.dao;

import no.auke.drone.domain.Drone;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Person;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface DroneFactory {
    Drone createDrone(String id, String name);

    Drone createDrone(String id, String name, MapPoint location);

    Drone createDrone(String id, String name, double altitude, double speed, long time, String droneType, Person flyer, boolean hasCamera, MapPoint location);
}
