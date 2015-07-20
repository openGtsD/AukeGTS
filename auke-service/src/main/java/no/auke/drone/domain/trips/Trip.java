package no.auke.drone.domain.trips;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import no.auke.drone.domain.MapPoint;
import no.auke.drone.utils.ByteUtil;

public class Trip {
	
	private UUID tripId;
	private String trackerId;	
	
	private long startTime;
	private long stopTime;		
	
	public String getTripId() {
		return tripId.toString();
	}

	public void setTripId(String tripId) {
		this.tripId = UUID.fromString(tripId);
	}

	public String getTrackerId() {
		return trackerId;
	}

	public void setTrackerId(String trackerId) {
		this.trackerId = trackerId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}

	// startpoint is first on route
	public TripPoint getStartPoint() {
		
		if(route.size()>0) {
			
			return route.get(0);
			
		} else {
			
			return new TripPoint();
		}
		
	}

	// stop is last on route
	public TripPoint getStopPoint() {
		
		if(route.size()>0) {
			
			return route.get(route.size() - 1 );
			
		} else {
			
			return new TripPoint();
		}
		
	}
	
	// get time of trip in seconds
	public int getTripTime() {
		
		if(route.size()>0) {
			
			return (int) ((route.get(route.size() - 1 ).getTime() - route.get(0).getTime()) / 1000);
			
		} else {
			
			return 0;
		}
		
	}
	
	// get distance of trip in meter
	public double getDistanceMeter() {
		
		// TODO: getDistanceMeter, to be implemented
		
		if(route.size()>0) {
			
			return 0;
			
		} else {
			
			return 0;
		}
		
	}
	
	
	// .... and more attributes to define 
	
	
	// internal storage of trip points
	private List<TripPoint> route = new ArrayList<TripPoint>();
	
	
	public List<TripPoint> getRoute() {
		return route;
	}

	public void setRoute(List<TripPoint> route) {
		this.route = route;
	}

	public Trip(String trackerId) {
		this.trackerId=trackerId;
		tripId=UUID.randomUUID();
	}
	
	public Trip(String trackerId, String tripid) {
		this.trackerId=trackerId;
		this.tripId=UUID.fromString(tripid);
	}	
	
	public void storePoint(MapPoint point) {
		route.add(new TripPoint(point));
	}

	public byte[] getRouteAsBytes() {
		
		if(route!=null && route.size()>0) {
			
			List<byte[]> positions = new ArrayList<byte[]>();
			for(TripPoint point:route){
				positions.add(point.getBytes());
			}
			return ByteUtil.mergeBytes(positions);
			
		} else {
			
			return null;
		
		}
	}
	
	public void setRouteAsBytes(byte[] data) {
		route.clear();
		List<byte[]> poslist = ByteUtil.splitDynamicBytes(data);
		for(byte[] pos:poslist){
			route.add(new TripPoint(data));
		}
	}
	

}
