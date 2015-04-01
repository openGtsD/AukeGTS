package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import no.auke.drone.dao.impl.SimpleDroneFactory;
import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Drone;
import no.auke.drone.domain.DroneData;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Observer;
import no.auke.drone.services.DroneService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by huyduong on 3/25/2015.
 */
@Service
public class DroneServiceImpl implements DroneService {
    private static final Logger logger = LoggerFactory.getLogger(DroneServiceImpl.class);

    Map<String, Drone> drones = new ConcurrentHashMap<String, Drone>();

    @PostConstruct
    public void initDroneService() {
        logger.info("initializing drone services");
        List<Drone> drones = new DroneServiceFacade().createDronesForCapitalCities();
        for (Drone drone : drones) {
            DroneData.getInstance().register((Observer) drone);
        }
        logger.info("finished initializing drone services");
    }

    public DroneServiceImpl() {

    }

    @Override
    public Drone registerDrone(String id, String name) {
        Drone drone = new SimpleDroneFactory().createDrone(id, name);
        DroneData.getInstance().register((Observer) drone);
        return drone;
    }

    @Override
    public Drone removeDrone(String droneId) {
        Drone drone = new SimpleDroneFactory().createDrone(droneId, "");
        DroneData.getInstance().remove((Observer) drone);
        return drone;
    }

    @Override
    public Drone getDrone(String id) {
        Drone drone = DroneData.getInstance().getDrone(id) != null ? (Drone) DroneData.getInstance().getDrone(id)
                : null;
        return drone;
    }

    @Override
    public Collection<Drone> getAll() {
        return DroneData.getInstance().getDrones().values();
    }

    @Override
    public Drone moveDrone(String id) {
        Drone drone = DroneData.getInstance().getDrone(id) != null ? (Drone) DroneData.getInstance().getDrone(id)
                : null;
        if (drone != null) {

        }
        return drone;
    }

    private class DroneServiceFacade {
        private List<Drone> createDroneForCity(int numberOfDrones, double latitude, double longitude) {
            List<Drone> result = new ArrayList<Drone>();
            for (int i = 0; i < numberOfDrones; i++) {
                result.add(new SimpleDroneFactory().createDrone(String.valueOf(i), String.valueOf(i),
                        new MapPoint(1, 1)));
            }
            return result;
        }

        public List<Drone> createDronesForCapitalCities() {
            List<Drone> result = new ArrayList<Drone>();
            // create drones for New York
            logger.info("creating 100 drones for New York");
            result.addAll(createDroneForCity(100,44.930,-74.893));

            // TODO update location for different cities
            // create drones for Paris
            // createDroneForCity(100,44.930,-74.893);

            return result;
        }

    }

    @Override
    public List<Drone> loadDroneWithinView(BoundingBox boundary) {
        List<Drone> result = new ArrayList<Drone>();
        for (Drone positionUnit : getAll()) {
            if (positionUnit.withinView(boundary.getNorthEastLat(), boundary.getNorthEastLon(),
                    boundary.getSouthWestLat(), boundary.getSouthWestLon())) {
                result.add(positionUnit);
            }
        }
        return result;
    }

}
