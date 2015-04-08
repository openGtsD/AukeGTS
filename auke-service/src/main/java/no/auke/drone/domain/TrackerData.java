package no.auke.drone.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import no.auke.drone.services.PositionCalculator;
import no.auke.drone.services.impl.PositionCalculatorImpl;

/**
 * Created by huyduong on 3/24/2015.
 */
public class TrackerData implements Subject {
    private Map<String, Tracker> trackers;
    private PositionCalculator positionCalculator;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
        }
        return executor;
    }

    private static TrackerData trackerData;

    public static synchronized TrackerData getInstance() {
        return getInstance(true);
    }

    public static synchronized TrackerData getInstance(boolean isRunningAutomatically) {
        if (trackerData == null) {
            trackerData = new TrackerData(isRunningAutomatically);
        }
        return trackerData;
    }

    private TrackerData(boolean isRunningAutomatically) {
        trackers = new ConcurrentHashMap<String, Tracker>();
        positionCalculator = new PositionCalculatorImpl(getExecutor(), trackers, isRunningAutomatically);
    }

    public PositionCalculator getPositionCalculator() {
        return positionCalculator;
    }

    public Map<String, Tracker> getTrackers() {
        return trackers;
    }

    public Tracker getDrone(String id) {
        return trackers.containsKey(id) ? trackers.get(id) : null;
    }

    @Override
    public void register(Observer drone) {
        trackers.put(drone.getId(), (Tracker) drone);
        positionCalculator.startCalculate();
    }

    @Override
    public void remove(Observer drone) {
        trackers.remove(drone.getId());
        if (trackers.size() == 0) {
            positionCalculator.stopCalculate();
        }
    }

    @Override
    public void notifyAllItems() {
        System.out.println("........notifying " + trackers.size() + " items........");
        for (String droneId : trackers.keySet()) {
            trackers.get(droneId).update();
        }
        System.out.println(".......notifying finished......");
    }
}
