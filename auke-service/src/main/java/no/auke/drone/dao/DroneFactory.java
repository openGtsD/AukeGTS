package no.auke.drone.dao;

import no.auke.drone.domain.Drone;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface DroneFactory {
    Drone createDrone(String id, String name);
}
