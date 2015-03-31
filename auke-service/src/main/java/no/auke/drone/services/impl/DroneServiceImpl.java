package no.auke.drone.services.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import no.auke.drone.dao.impl.SimpleDroneFactory;
import no.auke.drone.domain.*;
import no.auke.drone.services.DroneService;
import org.springframework.stereotype.Service;


/**
 * Created by huyduong on 3/25/2015.
 */
@Service
public class DroneServiceImpl implements DroneService {
    Map<String,Drone> drones = new ConcurrentHashMap<String,Drone>();



    public DroneServiceImpl() {

    }

    // LHA: not sure if use like this.
    @Override
    public void registerDrone(Drone drone) {
        // lha: anyway. Pull calculation to the drone
        DroneData.getInstance().register((Observer)drone);
    }

    @Override
    public void removeDrone(Drone drone) {
        DroneData.getInstance().remove((Observer)drone);
    }

    @Override
    public Drone createDrone(String id, String name) {
        Drone drone = new SimpleDroneFactory().createDrone(id,name);
        return drone;
    }
}
