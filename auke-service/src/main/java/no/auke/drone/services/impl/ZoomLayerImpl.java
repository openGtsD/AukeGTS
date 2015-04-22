package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.LayerHandling;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerLayer;
import no.auke.drone.services.ZoomLayer;

public class ZoomLayerImpl implements ZoomLayer, LayerHandling {

    private ReentrantLock block = new ReentrantLock();
    private Collection<MapPoint> positions = new ConcurrentLinkedQueue<MapPoint>();
	private TrackerLayer trackerLayer;
	private int zoomFactor;
	
    
    @Override
	public double zoomLongitude(Double longitude) {
    	long x = zoomFactor;
    	return 0;
    }

    @Override
	public double zoomLatitude(Double latitude) {
    	long x = zoomFactor;
    	return 0;
    }
    
	public ZoomLayerImpl(TrackerLayer trackerLayer, int zoomFactor){
		
		this.trackerLayer=trackerLayer;
		this.zoomFactor=zoomFactor;
		
	}
	
	@Override
	public void calculate() {
		
	    Map<String,MapPoint> new_positions = new HashMap<String,MapPoint>();
		
        for(Tracker tracker : trackerLayer.getTrackers()) {
        	
        	double lon = zoomLongitude(tracker.getCurrentPosition().getLongitude());
        	double lat = zoomLatitude(tracker.getCurrentPosition().getLatitude());

        	String index = String.valueOf(lon) + ":" + String.valueOf(lat);
        	
        	MapPoint point;
        	
        	if(!new_positions.containsKey(index)) {
        		
        		point=new MapPoint();
        		point.setLatitude(lat);
        		point.setLongitude(lon);
        		
        		new_positions.put(index, point);
        		
        	} else {

        		point = new_positions.get(index);
        		
        	}
        	point.incrementTrackers();

        }
        
        try {

            block.lock();

            positions.clear();
        	positions.addAll(new_positions.values());
            
        } finally {

            block.unlock();
        }
        
		
		
	}
	
    @Override
    public Collection<MapPoint> getPositions() {
        return positions;
    }

    @Override
    public List<Tracker> loadWithinView(BoundingBox boundary, int zoom) {

        try {

            block.lock();

            List<Tracker> result = new ArrayList<Tracker>();
//            for (Tracker positionUnit : trackers.values()) {
//
//                if (positionUnit.withinView(boundary.getSouthWestLat(), boundary.getSouthWestLon(),
//                        boundary.getNorthEastLat(), boundary.getNorthEastLon())) {
//
//                    result.add(positionUnit);
//                }
//            }
            return result;
            
        } finally {

            block.unlock();
        }
        
    }
    
}
