package no.auke.drone.dao.impl;

import no.auke.drone.dao.DroneFactory;
import no.auke.drone.dao.DummyCreator;
import no.auke.drone.domain.Drone;
import no.auke.drone.domain.MapPoint;
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
        return createDrone(id, name, new DummyCreator().makeLocation(100,100));
    }

    @Override
    public Drone createDrone(String id, String name, MapPoint location) {
        Drone drone = new SimpleDrone();
        drone.setId(id);
        drone.setName(name);
        drone.setCurrentPosition(DummyCreator.createPositionUnit(location));
        drone.getPositions().add(drone.getCurrentPosition());
        logger.info("created drone: " + drone.toString());
        return drone;
    }
}
