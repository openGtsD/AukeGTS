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
import no.auke.drone.services.ZoomLayerService;

public class ZoomLayerServiceImpl implements ZoomLayerService {

    private ReentrantLock block = new ReentrantLock();
    private Collection<Tracker> positions = new ConcurrentLinkedQueue<Tracker>();
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
		
	    Map<Long,Tracker> new_positions = new HashMap<Long,Tracker>();
		
        for(Tracker tracker : trackerLayer.getTrackers()) {
        	
        	double lon = zoomLongitude(tracker.getCurrentPosition().getLongitude());
        	double lat = zoomLatitude(tracker.getCurrentPosition().getLatitude());

        	Long index = getIndex(lon,lat);

        	Tracker point;
        	if(!new_positions.containsKey(index)) {
        		
        		point=new TrackerSum();
        		point.setId(String.valueOf(index));
        		point.setName("Tracker within long=" + String.valueOf(lon) +  " lat=" +String.valueOf(lat));
        		
        		point.getCurrentPosition().setLatitude(tracker.getCurrentPosition().getLatitude());
        		point.getCurrentPosition().setLongitude(tracker.getCurrentPosition().getLongitude());

        		new_positions.put(index, point);
        		
        	} else {

        		point = new_positions.get(index);
        		
        		
        	}
        	
    		
        	point.incrementTrackers();

        	// LHA: We show average positions for all trackers on current summarized positions
        	
    		point.getCurrentPosition().setLatitude(
    				( ( 
    					point.getCurrentPosition().getLatitude() * point.getNumtrackers()
    				  ) + tracker.getCurrentPosition().getLatitude() ) / 2
    				);  

    		point.getCurrentPosition().setLongitude(
    				( (
    					point.getCurrentPosition().getLongitude() * point.getNumtrackers() 
    				  ) + tracker.getCurrentPosition().getLongitude() ) / 2
    				);
        	
        	
        	// LHA: Don't think a good idea to serve all tracker info in each point
        	
        	// will destroy performance on low zoom levels
        	// On reason for doing a zoom summarize is t reduce
        	// size of query data to map (every 10 second)
        	// with a list of trackers on each point
        	// we still send a lot of information (same as without summarize)
        	
        	// Instead we should make a second call from UI
        	// to retrieve tracker information in each point when user click the point on map
        	
        	// Add this information when calculate, but remove from json sent to UI
        	// (add to trackerSUM class, instead if tracker class)
        	
            point.addInnerTrackers(tracker);
            
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
				
//				System.out.println("zoom"+getZoomFactor() + 
//						" getSouthWestLon " + boundary.getSouthWestLon() +
//						" getSouthWestLat " + boundary.getSouthWestLat() + 
//						" getNorthEastLon " + boundary.getNorthEastLon() + 
//						" getNorthEastLat " + boundary.getNorthEastLat()  
//						);
					

			}
		
		}	
		
		return boundaries;

	}
    
}
