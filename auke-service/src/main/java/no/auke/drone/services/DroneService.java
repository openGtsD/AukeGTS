package no.auke.drone.services;

import no.auke.drone.domain.Drone;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface DroneService {
    void registerDrone();
    void removeDrone();
    Drone createDrone();
}
