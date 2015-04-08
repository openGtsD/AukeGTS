package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    private Map<Tracker.TrackerType,Map<String,Tracker>> trackerLayers = new ConcurrentHashMap<Tracker.TrackerType,Map<String,Tracker>>();
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
        Map<Tracker.TrackerType,Map<String,Tracker>> trackerLayers = new ConcurrentHashMap<>();
        Map<String,Tracker> realLayer = new ConcurrentHashMap<>();
        Map<String,Tracker> simulatedLayer = new ConcurrentHashMap<>();

        trackerLayers.put(Tracker.TrackerType.REAL,realLayer);
        trackerLayers.put(Tracker.TrackerType.SIMULATED,simulatedLayer);

        positionCalculator = new PositionCalculatorImpl(getExecutor(), getTrackers(), isRunningAutomatically);
    }

    public PositionCalculator getPositionCalculator() {
        return positionCalculator;
    }

    private Map<String,Tracker> getTrackerLayer(Tracker.TrackerType trackerType) {
        Map<String,Tracker> trackerLayer = trackerLayers.get(trackerType);
        if(trackerLayer == null) {
            trackerLayer = new ConcurrentHashMap<>();
            trackerLayers.put(trackerType,trackerLayer);
        }
        return trackerLayer;
    }

    public Collection<Tracker> getTrackers() {
        return getTrackers(null);
    }

    public Collection<Tracker> getTrackers(Tracker.TrackerType trackerType) {
        List<Tracker> result = new ArrayList<>();

        if(trackerType == null) {
            result.addAll(getTrackerLayer(Tracker.TrackerType.REAL).values());
            result.addAll(getTrackerLayer(Tracker.TrackerType.SIMULATED).values());

        } else if(trackerType.equals(Tracker.TrackerType.REAL)) {
            result.addAll(getTrackerLayer(Tracker.TrackerType.REAL).values());

        } else if(trackerType.equals(Tracker.TrackerType.SIMULATED)) {
            result.addAll(getTrackerLayer(Tracker.TrackerType.SIMULATED).values());
        }

        return result;
    }

    public Tracker getDrone(String id) {
        Tracker tracker = getTrackerLayer(Tracker.TrackerType.REAL).get(id);
        if(tracker == null) {
            tracker = getTrackerLayer(Tracker.TrackerType.SIMULATED).get(id);
        }
        return tracker;
    }

    @Override
    public void register(Observer drone) {
        Tracker tracker = (Tracker) drone;
        getTrackerLayer(tracker.getDroneType()).put(tracker.getId(), tracker);
        positionCalculator.startCalculate();
    }

    @Override
    public void remove(Observer drone) {
        Tracker tracker = (Tracker) drone;
        getTrackerLayer(tracker.getDroneType()).remove(tracker.getId());
        if (getTrackers().size() == 0) {
            positionCalculator.stopCalculate();
        }
    }

    @Override
    public void notifyAllItems() {
        System.out.println("........notifying " + getTrackers().size() + " items........");
        for (Tracker tracker : getTrackers()) {
            tracker.update();
        }
        System.out.println(".......notifying finished......");
    }
}
