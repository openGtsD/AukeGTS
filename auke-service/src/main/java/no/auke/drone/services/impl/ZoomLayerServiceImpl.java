package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import no.auke.drone.domain.*;
import no.auke.drone.services.ZoomLayerService;

import org.apache.commons.lang.SerializationUtils;

public class ZoomLayerServiceImpl implements ZoomLayerService {

    private ReentrantLock block = new ReentrantLock();
    private Collection<Tracker> positions = new ConcurrentLinkedQueue<Tracker>();
    
    private Map<Long,TrackerSum> trackerSUM = new ConcurrentHashMap<Long,TrackerSum>();
	
    private TrackerLayer trackerLayer;
	private int zoomFactor;
	
	@Override
    public int getZoomFactor() {
		return zoomFactor;
	}
	
	// Zoom Longitude = ROUND( LONGITUDE / ( 360 / ( 2 ^^(ZOOMLEVEL-1) ) ) , 1) * 10
	// Zoom Latitude = ROUND( LATITUDE / ( 180 / ( 2 ^^(ZOOMLEVEL-1) ) ) , 1) * 10	

	@Override
	public double longitudeWith() {
		
		return 360 / Math.pow(2,zoomFactor - 1);
	}
	
	@Override
	public double latitudeWith() {
		
		return 180 / Math.pow(2,zoomFactor - 1);
	}	
	
	@Override
	public double zoomLongitude(Double longitude) {
    	
		return Math.round((longitude / longitudeWith()) * 10000 ) / 1000;
		
    }

    @Override
	public double zoomLatitude(Double latitude) {
 
		return Math.round((latitude / latitudeWith()) * 10000 ) / 1000;

    }

	@Override
	public double longitude(Double zoomlongitude) {
    	
		return Math.round((longitudeWith() / 10 ) * zoomlongitude * 100) / 100;
		
    }

    @Override
	public double latitude(Double zoomlatitude) {
 
		return Math.round((latitudeWith() / 10 ) * zoomlatitude * 100) / 100;

    }    
	public ZoomLayerServiceImpl(TrackerLayer trackerLayer, int zoomFactor){
		
		this.trackerLayer=trackerLayer;
		this.zoomFactor=zoomFactor;
		
	}
	
	@Override
	public void calculate() {
		
	    Map<Long,TrackerSum> new_positions = new HashMap<Long,TrackerSum>();
		
        for(Tracker tracker : trackerLayer.getTrackers()) {
        	
        	double lon = zoomLongitude(tracker.getCurrentPosition().getLongitude());
        	double lat = zoomLatitude(tracker.getCurrentPosition().getLatitude());

        	Long id = getIndex(lon,lat);

        	TrackerSum point;
        	if(!new_positions.containsKey(id)) {
        		
        		point=new TrackerSum();
        		point.setId(String.valueOf(id));
        		point.setName("Tracker within long=" + String.valueOf(lon) + " lat=" + String.valueOf(lat));
        		point.getCurrentPosition().setLatitude(tracker.getCurrentPosition().getLatitude());
        		point.getCurrentPosition().setLongitude(tracker.getCurrentPosition().getLongitude());

        		new_positions.put(id, point);
        		
        	} else {

        		point = new_positions.get(id);
        		
        	}
        	
    		
        	point.incrementTrackers();
        	
        	// LHA: use this to get trackers included in the point
        	// See function, getIncludedTracker(Id)


        	// LHA: We show average positions for all trackers on current summarized positions
        	
    		point.getCurrentPosition().setLatitude(
    				( ( 
    					point.getCurrentPosition().getLatitude() * point.getNumtrackers()
    				  ) + tracker.getCurrentPosition().getLatitude() ) / (point.getNumtrackers() + 1)
    				);  

    		point.getCurrentPosition().setLongitude(
    				( (
    					point.getCurrentPosition().getLongitude() * point.getNumtrackers() 
    				  ) + tracker.getCurrentPosition().getLongitude() ) / (point.getNumtrackers() + 1)
    				);
        	
        	
        	// LHA: use this to get trackers included in the point
        	// See function, getIncluded(Id)

            point.addInnerTrackers(tracker);
            
        }
        
        try {

            block.lock();
            
            // LHA: this will cause summarized tracker lit to grow out to big
            // 
//
//            
//            for(Tracker tracker : new_positions.values()) {
//                Tracker persistingTracker = new TrackerSum();
//                persistingTracker.setId(tracker.getId());
//                persistingTracker.setName(tracker.getName());
//                persistingTracker.setLayerId(Tracker.TrackerType.SUMMARIZED.toString());
//                persistingTracker.getInnerTrackers().addAll(tracker.getInnerTrackers());
//                tracker.getInnerTrackers().clear();
//                persistingTracker.setCurrentPosition(tracker.getCurrentPosition());
//                TrackerData.getInstance().register((Observer)persistingTracker);
//            }
            
           
            positions.clear();
            positions.addAll(new_positions.values());

            // for getting tracker info
            trackerSUM.clear();
            trackerSUM.putAll(new_positions);
            
            
        } finally {

            block.unlock();
        }
        
	}
	
    private Long getIndex(double lon, double lat) {
		return (long) ((lon * 100000000L) + lat);
	}

	@Override
    public Collection<Tracker> getPositions() {
        return positions;
    }

    @Override
    public Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom) {

    	// zoom factor not in use
    	Collection<Tracker> result = new ArrayList<Tracker>();
        
    	if(zoom == getZoomFactor()) {
    		
            try {

                block.lock();

                for (Tracker positionUnit : getPositions()) {

                    if (
                    		positionUnit.withinView
                    			(
                    				boundary.getSouthWestLat(), 
                    				boundary.getSouthWestLon(),
                    				boundary.getNorthEastLat(), 
                    				boundary.getNorthEastLon()
                    			)
                    	
                    	) {

                        result.add(positionUnit);
                    }
                }
                
                
            } finally {

                block.unlock();
            }
    		
    	}

    	return result;
        
    }
    
    // LHA: Use this to get the list of included trackers
    @Override
    public Collection<Tracker> getIncludedTrackers(String trackerId) {
    	try {
    		
        	Long id = Long.valueOf(trackerId);
        	if(trackerSUM.containsKey(id)) {
        		return trackerSUM.get(id).getIncludedTrackers();
        	}
    		
    	} catch (Exception ex) {
    	}
    	return null;
    	
    	
    }

	@Override
	public void clear() {
		
        try {

            block.lock();
    		positions.clear();
            
        } finally {

            block.unlock();
        }

        

	}

	@Override
	public List<BoundingBox> getMapAreas() {
		
		double lonsize = (360 / (Math.pow(2,(getZoomFactor()-1))));
		double latsize = (180 / (Math.pow(2,(getZoomFactor()-1))));
		
		List<BoundingBox> boundaries = new ArrayList<BoundingBox>();

		for(double lon=-180;lon<180;lon+=lonsize) {
		
			for(double lat=-90;lat<90;lat+=latsize) {
				BoundingBox boundary = new BoundingBox(lat,lon,lat+latsize,lon+lonsize);
				boundaries.add(boundary);
			}
		
		}	
		
		return boundaries;

	}
    
}
