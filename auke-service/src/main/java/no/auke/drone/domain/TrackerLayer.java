package no.auke.drone.domain;

import no.auke.drone.services.PositionCalculator;
import no.auke.drone.services.ZoomLayerService;
import no.auke.drone.services.impl.PositionCalculatorImpl;
import no.auke.drone.services.impl.TrackerServiceImpl;
import no.auke.drone.services.impl.ZoomLayerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huyduong on 4/10/2015.
 */


// TODO: LHA: Should be renamed to LayerService and moved to services name space

public class TrackerLayer  {

    private static final Logger logger = LoggerFactory.getLogger(TrackerLayer.class);

	private String layerName;
    private String id;

    // Layer list and layer handling
    private Map<Integer,ZoomLayerService> zoomLayers;
    public ZoomLayerService getZoomLayer(Integer zoom) {
        return zoomLayers.get(zoom);
    }

    public Collection<ZoomLayerService> getZoomLayers() {
		return zoomLayers.values();
	}
	public void setZoomLayers(Map<Integer, ZoomLayerService> zoomLayers) {
		this.zoomLayers = zoomLayers;
	}

	private Map<String,Tracker> activeTrackers;
    private Map<String,Tracker> passiveTrackers;

    private PositionCalculator positionCalculator;
    private boolean isRunningAutomatically;

    public boolean isRunningAutomatically() {
		return isRunningAutomatically;
	}
	public void setRunningAutomatically(boolean isRunningAutomatically) {
		this.isRunningAutomatically = isRunningAutomatically;
	}
	public TrackerLayer(String layerName, boolean isRunningAutomatically) {
        
    	this.layerName = layerName;
        this.isRunningAutomatically=isRunningAutomatically;
        
        id = UUID.randomUUID().toString();
        activeTrackers = Collections.synchronizedMap(new LinkedHashMap<String, Tracker>());
        passiveTrackers = Collections.synchronizedMap(new LinkedHashMap<String, Tracker>());

        // Adding zoom layer
        zoomLayers = new ConcurrentHashMap<Integer,ZoomLayerService>();
        for(int zoom = 1;zoom<11;zoom++) {
        	zoomLayers.put(zoom, new ZoomLayerServiceImpl(this,zoom));
        }
        
        // adding the position calculator 
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

    public synchronized Map<String,Tracker> getActiveTrackersMap() {
        if(activeTrackers == null) {
            activeTrackers = Collections.synchronizedMap(new LinkedHashMap<String, Tracker>());
        }

        return activeTrackers;
    }

    public Map<String,Tracker> getPassiveTrackersMap() {
        if(passiveTrackers == null) {
            passiveTrackers = Collections.synchronizedMap(new LinkedHashMap<String, Tracker>());
        }

        return passiveTrackers;
    }

    public Collection<Tracker> getTrackers() {
        Collection<Tracker> trackers = new ArrayList<>();
        trackers.addAll(getActiveTrackers());
        trackers.addAll(getPassiveTrackers());

        return trackers;
    }

    public synchronized Collection<Tracker> getActiveTrackers() {
        return new ArrayList<>(getActiveTrackersMap().values());
    }

    public synchronized Collection<Tracker> getPassiveTrackers() {
        return new ArrayList<>(getPassiveTrackersMap().values());
    }

    public synchronized void addTracker(Tracker tracker) {
        getActiveTrackersMap().put(tracker.getId(), tracker);
        getPassiveTrackersMap().remove(tracker.getId());
        if(isRunningAutomatically) {
        	positionCalculator.startCalculate();      	
        }
        
    }

    public synchronized void removeTracker(Tracker tracker) {

        getActiveTrackersMap().remove(tracker.getId());
        getPassiveTrackersMap().remove(tracker.getId());

        if (getActiveTrackersMap().size() == 0) {
            positionCalculator.stopCalculate();
        }
    }

    public synchronized void updateActiveTracker(Tracker tracker) {
        if(tracker.isActive()) {
            if(!getActiveTrackersMap().containsKey(tracker.getId())) {
                getActiveTrackersMap().put(tracker.getId(),tracker);
            }

            if(getPassiveTrackersMap().containsKey(tracker.getId())) {
                getPassiveTrackersMap().remove(tracker.getId());
            }
        } else if(!tracker.isActive()) {
            if(getActiveTrackersMap().containsKey(tracker.getId())) {
                getActiveTrackersMap().remove(tracker.getId());
            }

            if(!getPassiveTrackersMap().containsKey(tracker.getId())) {
                getPassiveTrackersMap().put(tracker.getId(),tracker);
            }
        }

        if (getActiveTrackersMap().size() == 0) {
            positionCalculator.stopCalculate();
        } else {
            positionCalculator.startCalculate();
        }
    }

    public Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom) {
        try {
            if(zoomLayers.containsKey(zoom)) { 
             	return zoomLayers.get(zoom).loadWithinView(boundary, zoom);
            } else {
            	List<Tracker> result = new ArrayList<Tracker>();
                // LHA: got tracker from there
                for (Tracker positionUnit : getActiveTrackers()) {

                    if (positionUnit.withinView(boundary.getSouthWestLat(), boundary.getSouthWestLon(),
                            boundary.getNorthEastLat(), boundary.getNorthEastLon())) {
                        result.add(positionUnit);
                    }
                }
                
                return result;
            }
        	
        } catch (Exception ex ) {
        	logger.warn("loadWithinView: error ",ex);
        }
        
        return new ArrayList<Tracker>();
    }

	public boolean exists(String id) {
		return getActiveTrackersMap().containsKey(id) || getPassiveTrackersMap().containsKey(id);
	}

	public Tracker getTracker(String trackerId) {
		Tracker tracker = getActiveTrackersMap().get(trackerId);
        if(tracker == null) {
            tracker = getPassiveTrackersMap().get(trackerId);
        }
        return tracker;
	}

	public void startCalculate() {
		isRunningAutomatically=true;
		if(getActiveTrackersMap().size()>0) {
	    	positionCalculator.startCalculate();
		}
	}

	public Collection<Tracker> getPositions() {
		return activeTrackers.values();
	}
	
	//
	// do all calculations
	// 
	public void calculateAll() {
		calulateTrackerFeed();
		calculateZoomLayers();
	}
	
	//
	// calculate incoming tracker feed
	// ref. from open GTS 
	// 
	public void calulateTrackerFeed() {
		for(Tracker tracker : getActiveTrackers()) {
            tracker.calculate();
        }
	}

	//
	// Summarize all positions to zoomlayers
	// 
	public void calculateZoomLayers() {
        // LHA: calculate zoomLayers
		for(ZoomLayerService serv:getZoomLayers()) {
			serv.calculate();
		}
		
	}

}
