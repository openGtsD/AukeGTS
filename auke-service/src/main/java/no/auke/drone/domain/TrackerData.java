package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private List<TrackerLayer> trackerLayers;
    
	public List<TrackerLayer> getLayers() {
		return trackerLayers;
	}    

    public static synchronized TrackerData getInstance() {
    	if (trackerData==null) {
    		trackerData = new TrackerData();
        }
        return trackerData;
    }    		

    private TrackerData() {
    	trackerLayers = new ArrayList<>();
    	trackerLayers.add(new TrackerLayer("REAL"));
        trackerLayers.add(new TrackerLayer("SIMULATED"));
    }

    private TrackerLayer getTrackerLayer(String layerId) {
    	for(TrackerLayer trackerLayer : trackerLayers) {
            if(layerId.equalsIgnoreCase(trackerLayer.getLayerName())) {
                return trackerLayer;
            }
        }
    	
    	// add a new layer
    	TrackerLayer new_layer = new TrackerLayer(layerId);
    	trackerLayers.add(new_layer);
        return new_layer;
        
    }

    public Collection<Tracker> getTrackers() {
        return getTrackers(null);
    }

    public Collection<Tracker> getTrackers(Tracker.TrackerType trackerType) {
    	
    	List<Tracker> result = new ArrayList<>();

        if(trackerType == null || trackerType.equals(Tracker.TrackerType.REAL)) {
        	result.addAll(getTrackerLayer(Tracker.TrackerType.REAL.toString()).getTrackers().values());
        }

        if(trackerType == null || trackerType.equals(Tracker.TrackerType.SIMULATED)) {
            result.addAll(getTrackerLayer(Tracker.TrackerType.SIMULATED.toString()).getTrackers().values());
        }

        return result;
    }

    private void update(Tracker oldTracker, Tracker newTracker) {
        
    	oldTracker.setName(newTracker.getName());
        oldTracker.setUsedCamera(newTracker.isUsedCamera());
        oldTracker.setTrackerType(newTracker.getTrackerType());
        oldTracker.setFlyer(newTracker.getFlyer());
    
    }

    public Tracker update(Tracker newTracker) {
        
    	Tracker tracker = getTracker(newTracker.getId());
        if(tracker.equals(newTracker)) {
            return tracker;// do nothing
        }

        update(tracker,newTracker);
        return tracker;
    }

    public Tracker getTracker(String id) {
    	
    	for(TrackerLayer trackerLayer : trackerLayers) {
    		if(trackerLayer.getTrackers().containsKey(id)) {
    			return trackerLayer.getTrackers().get(id);
    		}
    	}
    	return null;
    
    }

    @Override
    public void register(Observer drone) {
    
    	Tracker tracker = (Tracker) drone;
    	
    	if(tracker.getLayerid()!=null) {
    		getTrackerLayer(tracker.getLayerid()).addTracker(tracker);

    	} else if (tracker.getTrackerType()!=null){
    		getTrackerLayer(tracker.getTrackerType().toString()).addTracker(tracker);
    	}
    
    }

    @Override
    public void remove(Observer drone) {
    	Tracker tracker = (Tracker) drone;

    	if(tracker.getLayerid()!=null) {
            getTrackerLayer(tracker.getLayerid()).removeTracker(tracker);
    	} else if (tracker.getTrackerType()!=null){
            getTrackerLayer(tracker.getTrackerType().toString()).removeTracker(tracker);
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


}
