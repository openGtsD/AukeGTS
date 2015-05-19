package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.auke.drone.services.PositionCalculator;
import no.auke.drone.services.ZoomLayerService;
import no.auke.drone.services.impl.PositionCalculatorImpl;
import no.auke.drone.services.impl.TrackerServiceImpl;
import no.auke.drone.services.impl.ZoomLayerServiceImpl;

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
    public Collection<ZoomLayerService> getZoomLayers() {
		return zoomLayers.values();
	}
	public void setZoomLayers(Map<Integer, ZoomLayerService> zoomLayers) {
		this.zoomLayers = zoomLayers;
	}

	private Map<String,Tracker> trackers;

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
        trackers = new ConcurrentHashMap<String,Tracker>();

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

    public Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom) {

        
        try {
        	
            if(zoomLayers.containsKey(zoom)) { 
            //for testing purpose, temporary removing this from current version
            //if(false) {
                // LHA: if summarized exists, got trackerSUM from there
            	return zoomLayers.get(zoom).loadWithinView(boundary, zoom);
            	
            } else {

            	List<Tracker> result = new ArrayList<Tracker>();
            	
                // LHA: got tracker from there
                for (Tracker positionUnit : trackers.values()) {

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

	public Collection<Tracker> getPositions() {
		return trackers.values();
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
		for(Tracker tracker : trackers.values()) {
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
