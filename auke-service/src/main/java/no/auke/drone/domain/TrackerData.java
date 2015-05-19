package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import no.auke.drone.application.impl.SimpleTrackerFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/24/2015.
 */


// LHA: maybe this object is the layer head ??
// 

public class TrackerData implements Subject {
	
	private static final Logger logger = LoggerFactory.getLogger(TrackerData.class);

    private static TrackerData trackerData;
    private Map<String,TrackerLayer> trackerLayers;
    
	public Collection<TrackerLayer> getLayers() {
		return trackerLayers.values();
	}    

    public static synchronized TrackerData getInstance() {
    	if (trackerData==null) {
    		trackerData = new TrackerData();
        }
        return trackerData;
    }    		
    
	public static void clear() {
		trackerData=null;
	}    

    private TrackerData() {
    	trackerLayers = new ConcurrentHashMap<String,TrackerLayer>();
    	trackerLayers.put("REAL",new TrackerLayer("REAL"));
        trackerLayers.put("SIMULATED",new TrackerLayer("SIMULATED",true)); // for testing only
    }

    public TrackerLayer getTrackerLayer(String layerId) {
    	
    	TrackerLayer layer = trackerLayers.get(StringUtils.upperCase(layerId));
    	if(layer == null) {
        	// add a new layer
        	layer = new TrackerLayer(layerId);
        	trackerLayers.put(StringUtils.upperCase(layer.getLayerName()),layer);
    	}
    	return layer;	
        
    }

    public Collection<Tracker> getTrackers() {
        return getTrackers(null);
    }

    public Collection<Tracker> getActiveTrackers() {
    	
    	// LHA: we need distinct between active and passive trackers
    	return getTrackers(null);
    }
    
    public Collection<Tracker> getTrackers(String layerId) {
    	List<Tracker> result = new ArrayList<>();

        if(StringUtils.isEmpty(layerId)) {
        	for(TrackerLayer layer:trackerLayers.values()) {
        		result.addAll(layer.getTrackers());
        	}
        } else {
        	result.addAll(getTrackerLayer(layerId).getTrackers());
        }

        return result;
    }

    private void update(Tracker oldTracker, Tracker newTracker) {
        
    	oldTracker.setName(newTracker.getName());
        oldTracker.setUsedCamera(newTracker.isUsedCamera());
        oldTracker.setTrackerType(newTracker.getTrackerType());
        oldTracker.setFlyer(newTracker.getFlyer());
        oldTracker.setSimPhone(newTracker.getSimPhone());
        oldTracker.setModifiedDate(new Date());
    
    }

    public Tracker update(Tracker newTracker) {
    	Tracker tracker = getTracker(newTracker.getId());
    	if(tracker == null) {
    	    tracker = new SimpleTrackerFactory().create(newTracker.getId(), newTracker.getName());
            TrackerData.getInstance().register((Observer) tracker);
    	}
    	
        if(tracker.equals(newTracker)) {
            return tracker;// do nothing
        }

        update(tracker,newTracker);
        return tracker;
    }

    public Tracker getTracker(String id) {
    	
    	for(TrackerLayer trackerLayer : trackerLayers.values()) {
    		if(trackerLayer.exists(id)) {
    			return trackerLayer.getTracker(id);
    		}
    	}
    	return null;
    
    }

    @Override
    public void register(Observer tracker) {
    	if(tracker.getLayerId()!=null) {
    		getTrackerLayer(tracker.getLayerId()).addTracker((Tracker) tracker);
    	}
    }

    @Override
    public void remove(Observer tracker) {
    	if(tracker.getLayerId()!=null) {
    		getTrackerLayer(tracker.getLayerId()).removeTracker((Tracker)tracker);
    	}
    }

    @Override
    public void notifyAllItems() {
        
    	logger.info("........notifying " + getTrackers().size() + " items........");
        for (Tracker tracker : getTrackers()) {
            tracker.update();
        }
        logger.info(".......notifying finished......");
    
    }

	public boolean exists(String layerId) {
		return trackerLayers.containsKey(layerId);
	}

	public void startCalculate() {
        logger.info("starting calculating " + trackerLayers.size() + " layer(s)");
    	for(TrackerLayer trackerLayer : trackerLayers.values()) {
    		trackerLayer.startCalculate();
    	}
        logger.info("finished calculating " + trackerLayers.size() + " layer(s)");

    }


}
