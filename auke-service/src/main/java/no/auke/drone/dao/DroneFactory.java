package no.auke.drone.dao;

import no.auke.drone.domain.Drone;
import no.auke.drone.domain.MapPoint;


/**
 * Created by huyduong on 3/25/2015.
 */
public interface DroneFactory {
    Drone createDrone(String id, String name);
    Drone createDrone(String id, String name, MapPoint location);
}
