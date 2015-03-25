package no.auke.drone.dao.impl;

import no.auke.drone.dao.DroneFactory;
import no.auke.drone.dao.DummyCreator;
import no.auke.drone.domain.Drone;
import no.auke.drone.domain.SimpleDrone;

/**
 * Created by huyduong on 3/25/2015.
 */
public class SimpleDroneFactory implements DroneFactory {
    @Override
    public Drone createDrone(String id, String name) {
        Drone drone = new SimpleDrone();
        drone.setId(id);
        drone.setName(name);
        drone.setCurrentPosition(DummyCreator.createPositionUnit(new DummyCreator().makeLocation(100,100)));
        return drone;
    }
}
