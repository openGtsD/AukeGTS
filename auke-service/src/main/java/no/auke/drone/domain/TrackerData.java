package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.auke.drone.services.PositionCalculator;
import no.auke.drone.services.impl.PositionCalculatorImpl;
import no.auke.drone.services.impl.TrackerServiceImpl;


/**
 * Created by huyduong on 3/24/2015.
 */


// LHA: maybe this object is the layer head ??
// 

public class TrackerData implements Subject {
	
	private static final Logger logger = LoggerFactory.getLogger(TrackerData.class);

    private static Map<String,TrackerData> trackerDataList = new ConcurrentHashMap<String,TrackerData>();

    public static synchronized TrackerData getInstance() {
        return getInstance("default",true);
    }    		
    public static synchronized TrackerData getInstance(String trackerLayer) {
        return getInstance(trackerLayer,true);
    }

    public static synchronized TrackerData getInstance(String trackerLayer, boolean isRunningAutomatically) {
        
    	if (!trackerDataList.containsKey(trackerLayer)) {
    		trackerDataList.put(trackerLayer, new TrackerData(trackerLayer,isRunningAutomatically));
        }
        return trackerDataList.get(trackerLayer);
    
    }

	private Map<Tracker.TrackerType,Map<String,Tracker>> trackerLayers = new ConcurrentHashMap<Tracker.TrackerType,Map<String,Tracker>>();
    private PositionCalculator positionCalculator;
    
    private TrackerData(String trackerLayer, boolean isRunningAutomatically) {
        
    	Map<Tracker.TrackerType,Map<String,Tracker>> trackerLayers = new ConcurrentHashMap<>();
        Map<String,Tracker> realLayer = new ConcurrentHashMap<>();
        Map<String,Tracker> simulatedLayer = new ConcurrentHashMap<>();

        trackerLayers.put(Tracker.TrackerType.REAL,realLayer);
        trackerLayers.put(Tracker.TrackerType.SIMULATED,simulatedLayer);

        positionCalculator = new PositionCalculatorImpl(TrackerServiceImpl.getExecutor(), getTrackers(), isRunningAutomatically);
    
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

    private void update(Tracker oldTracker, Tracker newTracker) {
        
    	oldTracker.setName(newTracker.getName());
        oldTracker.setUsedCamera(newTracker.isUsedCamera());
        oldTracker.setDroneType(newTracker.getDroneType());
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
        
    	logger.info("........notifying " + getTrackers().size() + " items........");
        for (Tracker tracker : getTrackers()) {
            tracker.update();
        }
        logger.info(".......notifying finished......");
    
    }
}
