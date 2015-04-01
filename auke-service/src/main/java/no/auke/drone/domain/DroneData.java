package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import no.auke.drone.dao.impl.SimpleDroneFactory;
import no.auke.drone.services.PositionCalculator;
import no.auke.drone.services.impl.PositionCalculatorImpl;

import com.auke.drone.ws.util.PointUtil;

/**
 * Created by huyduong on 3/24/2015.
 */
public class DroneData implements Subject {
    private Map<String, Drone> drones;
    private PositionCalculator positionCalculator;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
        }
        return executor;
    }

    private static DroneData droneData;

    public static synchronized DroneData getInstance() {
        return getInstance(true);
    }

    public static synchronized DroneData getInstance(boolean isRunningAutomatically) {
        if (droneData == null) {
            droneData = new DroneData(isRunningAutomatically);
        }
        return droneData;
    }

    private DroneData(boolean isRunningAutomatically) {
        drones = new ConcurrentHashMap<String, Drone>();
        makeRandomDrones(drones);
        positionCalculator = new PositionCalculatorImpl(getExecutor(), drones, isRunningAutomatically);
    }

    public PositionCalculator getPositionCalculator() {
        return positionCalculator;
    }

    public Map<String, Drone> getDrones() {
        return drones;
    }

    public Drone getDrone(String id) {
        return drones.containsKey(id) ? drones.get(id) : null;
    }

    @Override
    public void register(Observer drone) {
        drones.put(drone.getId(), (Drone) drone);
        positionCalculator.startCalculate();
    }

    @Override
    public void remove(Observer drone) {
        drones.remove(drone.getId());
        if (drones.size() == 0) {
            positionCalculator.stopCalculate();
        }
    }

    @Override
    public void notifyAllItems() {
        System.out.println("........notifying " + drones.size() + " items........");
        for (String droneId : drones.keySet()) {
            drones.get(droneId).update();
        }
        System.out.println(".......notifying finished......");
    }

    // Thai Huynh for test
    public void makeRandomDrones(Map<String, Drone> drones) {
        List<MapPoint> points = new ArrayList<MapPoint>();
        points.add(new MapPoint(10.823099, 106.629664));// HCM
        points.add(new MapPoint(59.913869, 10.752245));// OSLO
        points.add(new MapPoint(55.378051, -3.435973));// UK
        points.add(new MapPoint(51.507351, -0.127758));// London

        for (int i = 0; i < points.size(); i++) {
            MapPoint point = points.get(i);
            for (int j = 1; j <= 10; j++) {
                MapPoint rd = PointUtil.generateRandomMapPoint(point);
                Drone drone = new SimpleDroneFactory().createDrone(UUID.randomUUID().toString(), "Drone" + i + "-" + j,
                        2 * i, 2 * i, System.currentTimeMillis(), "type", null, true, rd);
                register((Observer) drone);
            }
        }
    }
}
