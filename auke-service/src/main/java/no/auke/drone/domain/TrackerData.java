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
    	trackerLayers = new ConcurrentHashMap<>();
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
    	return getActiveTrackers(null);
    }

    public Collection<Tracker> getActiveTrackers(String layerId) {
        return getTrackerLayer(layerId).getActiveTrackers();
    }

    public Collection<Tracker> getPassiveTrackers() {
        return getPassiveTrackers(null);
    }

    public Collection<Tracker> getPassiveTrackers(String layerId) {
        return getTrackerLayer(layerId).getPassiveTrackers();
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

    private void updateLayerReference(Tracker oldTracker, Tracker newTracker) {
        if(!StringUtils.trimToEmpty(oldTracker.getLayerId()).equals(StringUtils.trimToEmpty(newTracker.getLayerId()))) {
            getTrackerLayer(oldTracker.getLayerId()).removeTracker(oldTracker);
            getTrackerLayer(newTracker.getLayerId()).addTracker(oldTracker);
            oldTracker.setLayerId(newTracker.getLayerId());
        }
    }


    private void update(Tracker oldTracker, Tracker newTracker) {
        
    	oldTracker.setName(newTracker.getName());
        oldTracker.setDescription(newTracker.getDescription());
        oldTracker.setContactInfo(newTracker.getContactInfo());
        oldTracker.setStoredTrips(newTracker.isStoredTrips());
        oldTracker.setTrackerPrefix(newTracker.getTrackerPrefix());
        oldTracker.setImeiNumber(newTracker.getImeiNumber());

        oldTracker.setTrackerType(newTracker.getTrackerType());
        oldTracker.setOwner(newTracker.getOwner());
        oldTracker.setSimPhone(newTracker.getSimPhone());
        oldTracker.setModifiedDate(new Date());

        updateLayerReference(oldTracker,newTracker);
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

    public Tracker getTracker(String trackerId) {
    	
    	for(TrackerLayer trackerLayer : trackerLayers.values()) {
    		if(trackerLayer.exists(trackerId)) {
    			return trackerLayer.getTracker(trackerId);
    		}
    	}
    	return null;
    
    }

    public Tracker register(Observer tracker) {
    	if(tracker.getLayerId()!=null) {
    		getTrackerLayer(tracker.getLayerId()).addTracker((Tracker) tracker);
    	}
        return (Tracker) tracker;
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
    	for(TrackerLayer trackerLayer : trackerLayers.values()) {
    		trackerLayer.startCalculate();
    	}

    }


}
