package no.auke.drone.domain;

import no.auke.drone.services.PositionCalculator;
import no.auke.drone.services.PositionCalculatorImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huyduong on 3/24/2015.
 */
public class DroneData implements Subject {
    private Map<String,Drone> drones;
    private PositionCalculator positionCalculator;
    private ExecutorService executor = Executors.newCachedThreadPool();
    public ExecutorService getExecutor() {
        if(executor==null) {
            executor = Executors.newCachedThreadPool();
        }
        return executor;
    }

    private static DroneData droneData;

    public static synchronized DroneData getInstance() {
        return getInstance(true);
    }

    public static synchronized DroneData getInstance(boolean isRunningAutomatically) {
        if(droneData == null) {
            droneData = new DroneData(isRunningAutomatically);
        }
        return droneData;
    }

    private DroneData(boolean isRunningAutomatically) {
        drones = new ConcurrentHashMap<String,Drone>();
        positionCalculator = new PositionCalculatorImpl(getExecutor(),drones, isRunningAutomatically);
    }

    public PositionCalculator getPositionCalculator() {
        return positionCalculator;
    }

    public Map<String,Drone> getDrones() {
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
        if(drones.size() == 0) {
            positionCalculator.stopCalculate();
        }
    }

    @Override
    public void notifyAllItems() {
        System.out.println("........notifying " + drones.size() + " items........");
        for(String droneId : drones.keySet()) {
            drones.get(droneId).update();
        }
        System.out.println(".......notifying finished......");
    }
}
