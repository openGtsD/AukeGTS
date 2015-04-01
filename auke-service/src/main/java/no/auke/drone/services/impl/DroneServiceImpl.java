package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

import com.auke.drone.ws.util.PointUtil;

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
        public List<Drone> createDronesForCapitalCities() {
            List<Drone> result = new ArrayList<Drone>();
            List<MapPoint> points = new ArrayList<MapPoint>();
            points.add(new MapPoint(10.823099, 106.629664));// HCM
            points.add(new MapPoint(59.913869, 10.752245));// OSLO
            points.add(new MapPoint(55.378051, -3.435973));// UK
            points.add(new MapPoint(51.507351, -0.127758));// London
            points.add(new MapPoint(56.130366, -106.346771));//Canada
            
            for (int i = 0; i < points.size(); i++) {
                MapPoint point = points.get(i);
                for (int j = 1; j <= 10; j++) {
                    MapPoint rd = PointUtil.generateRandomMapPoint(point);
                    Drone drone = new SimpleDroneFactory().createDrone(UUID.randomUUID().toString(), "Drone" + i + "-"
                            + j, 2 * j, 2 * j, System.currentTimeMillis(), "type", null, true, rd);
                    result.add(drone);
                }
            }
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
