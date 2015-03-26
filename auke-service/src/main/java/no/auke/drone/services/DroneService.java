package no.auke.drone.services;

import no.auke.drone.domain.Drone;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface DroneService {
    void registerDrone(Drone drone);
    void removeDrone(Drone drone);
    Drone createDrone(String id, String name);
}
