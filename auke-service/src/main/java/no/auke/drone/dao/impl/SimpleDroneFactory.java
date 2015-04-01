package no.auke.drone.dao.impl;

import no.auke.drone.dao.DroneFactory;
import no.auke.drone.domain.Drone;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Person;
import no.auke.drone.domain.SimpleDrone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/25/2015.
 */
public class SimpleDroneFactory implements DroneFactory {
    private static final Logger logger = LoggerFactory.getLogger(SimpleDroneFactory.class);

    @Override
    public Drone createDrone(String id, String name) {
        return createDrone(id, name, new MapPoint(100, 100));
    }

    @Override
    public Drone createDrone(String id, String name, MapPoint location) {
        Drone drone = new SimpleDrone();
        drone.setId(id);
        drone.setName(name);
        drone.setCurrentPosition(location);
        drone.getPositions().add(drone.getCurrentPosition());
        logger.info("created drone: " + drone.toString());
        return drone;
    }

    @Override
    public Drone createDrone(String id, String name, double altitude, double speed, long time, String droneType,
            Person flyer, boolean hasCamera, MapPoint location) {
        Drone drone = new SimpleDrone();
        drone.setId(id);
        drone.setName(name);
        drone.setAltitude(altitude);
        drone.setSpeed(speed);
        drone.setDroneType(droneType);
        drone.setFlyer(flyer);
        drone.setUsedCamera(hasCamera);
        drone.setCurrentPosition(location);
        drone.getPositions().add(drone.getCurrentPosition());
        return drone;
    }

}
