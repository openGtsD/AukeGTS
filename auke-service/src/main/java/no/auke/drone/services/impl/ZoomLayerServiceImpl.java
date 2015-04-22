package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerLayer;
import no.auke.drone.domain.TrackerSum;
import no.auke.drone.services.LayerHandling;
import no.auke.drone.services.ZoomLayerService;

public class ZoomLayerServiceImpl implements ZoomLayerService, LayerHandling {

    private ReentrantLock block = new ReentrantLock();
    private Collection<Tracker> positions = new ConcurrentLinkedQueue<Tracker>();
	private TrackerLayer trackerLayer;
	private int zoomFactor;
	
	@Override
    public int getZoomFactor() {
		return zoomFactor;
	}

	@Override
	public double zoomLongitude(Double longitude) {
    	long x = zoomFactor;
    	return longitude;
    }

    @Override
	public double zoomLatitude(Double latitude) {
    	long x = zoomFactor;
    	return latitude;
    }
    
	public ZoomLayerServiceImpl(TrackerLayer trackerLayer, int zoomFactor){
		
		this.trackerLayer=trackerLayer;
		this.zoomFactor=zoomFactor;
		
	}
	
	@Override
	public void calculate() {
		
	    Map<Long,Tracker> new_positions = new HashMap<Long,Tracker>();
		
        for(Tracker tracker : trackerLayer.getTrackers()) {
        	
        	double lon = zoomLongitude(tracker.getCurrentPosition().getLongitude());
        	double lat = zoomLatitude(tracker.getCurrentPosition().getLatitude());

        	Tracker point;
        	if(!new_positions.containsKey(getIndex(lon,lat))) {
        		
        		point=new TrackerSum();
        		point.setId(String.valueOf(getIndex(lon,lat)));
        		point.setName("Tracker withing long=" + String.valueOf(lon) +  " lat=" +String.valueOf(lat));
        		
        		point.getCurrentPosition().setLatitude(lat);
        		point.getCurrentPosition().setLongitude(lon);
        		
        		new_positions.put(getIndex(lon,lat), point);
        		
        	} else {

        		point = new_positions.get(getIndex(lon,lat));
        		
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
	
    private Long getIndex(double lon, double lat) {
		return (long) ((lon * 10000000L) + (lat * 10000000L));
	}

	@Override
    public Collection<Tracker> getPositions() {
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
