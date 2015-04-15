package no.auke.drone.domain;

import no.auke.drone.services.PositionCalculator;
import no.auke.drone.services.impl.PositionCalculatorImpl;
import no.auke.drone.services.impl.TrackerServiceImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huyduong on 4/10/2015.
 */
public class TrackerLayer {
    private String layerName;
    private String id;

    private Map<String,Tracker> trackers;

    private PositionCalculator positionCalculator;


    public TrackerLayer(String layerName, boolean isRunningAutomatically) {
        this.layerName = layerName;
        id = UUID.randomUUID().toString();
        trackers = new ConcurrentHashMap<>();
        positionCalculator = new PositionCalculatorImpl(TrackerServiceImpl.getExecutor(), this, isRunningAutomatically);
    }

    public TrackerLayer(String layerName) {
        this(layerName,true);
    }

    public TrackerLayer() {
        this("default", true);
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public Map<String, Tracker> getTrackers() {
        if(trackers == null) {
            trackers = new HashMap<>();
        }
        return trackers;
    }

    public void setTrackers(Map<String, Tracker> trackers) {
        this.trackers = trackers;
    }

    public void addTracker(Tracker tracker) {
        getTrackers().put(tracker.getId(),tracker);
        positionCalculator.startCalculate();
    }

    public void removeTracker(Tracker tracker) {
        getTrackers().remove(tracker.getId());
        if (getTrackers().size() == 0) {
            positionCalculator.stopCalculate();
        }
    }

    public List<Tracker> loadWithinView(BoundingBox boundary, Tracker.TrackerType layerId) {

        List<Tracker> result = new ArrayList<Tracker>();
        for (Tracker positionUnit : trackers.values()) {

            if (positionUnit.withinView(boundary.getSouthWestLat(), boundary.getSouthWestLon(),
                    boundary.getNorthEastLat(), boundary.getNorthEastLon())) {

                result.add(positionUnit);
            }
        }
        return result;
    }
}
