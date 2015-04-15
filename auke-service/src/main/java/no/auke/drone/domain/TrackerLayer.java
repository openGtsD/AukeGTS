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
    private boolean isRunningAutomatically;

    public TrackerLayer(String layerName, boolean isRunningAutomatically) {
        
    	this.layerName = layerName;
        this.isRunningAutomatically=isRunningAutomatically;
        
        id = UUID.randomUUID().toString();
        trackers = new ConcurrentHashMap<>();
    
        positionCalculator = new PositionCalculatorImpl(TrackerServiceImpl.getExecutor(), this, isRunningAutomatically);
    
    }

    public TrackerLayer(String layerName) {
        this(layerName,false);
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

    public Collection<Tracker> getTrackers() {
        if(trackers == null) {
            trackers = new HashMap<>();
        }
        return trackers.values();
    }

    public void setTrackers(Map<String, Tracker> trackers) {
        this.trackers = trackers;
    }

    public void addTracker(Tracker tracker) {
        if(trackers == null) {
            trackers = new HashMap<>();
        }

        trackers.put(tracker.getId(),tracker);
        if(isRunningAutomatically) {
        	positionCalculator.startCalculate();
        	
        }
        
    }

    public void removeTracker(Tracker tracker) {
    	trackers.remove(tracker.getId());
        if (trackers.size() == 0) {
            positionCalculator.stopCalculate();
        }
    }

    public List<Tracker> loadWithinView(BoundingBox boundary, int zoom) {

        List<Tracker> result = new ArrayList<Tracker>();
        for (Tracker positionUnit : trackers.values()) {

            if (positionUnit.withinView(boundary.getSouthWestLat(), boundary.getSouthWestLon(),
                    boundary.getNorthEastLat(), boundary.getNorthEastLon())) {

                result.add(positionUnit);
            }
        }
        return result;
    }

	public boolean exists(String id) {
		return trackers.containsKey(id);
	}

	public Tracker getTracker(String layerId) {
		return trackers.get(layerId);
	}

	public void startCalculate() {
		isRunningAutomatically=true;
		if(trackers.size()>0) {
	    	positionCalculator.startCalculate();
		}
	}
}
