package no.auke.drone.domain.trips;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import no.auke.drone.domain.MapPoint;
import no.auke.drone.utils.ByteUtil;

public class Trip {
	
	private UUID tripId;
	private String trackerId;	
	
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
		
		if(route.size()>0) {
		
			return route.get(0).getTime();

		} else {
			
			return 0;
		}

	}

	public long getStopTime() {
		if(route.size()>0) {
			
			return route.get(route.size() - 1).getTime();

		} else {
			
			return 0;
		}
	}

	// startpoint is first on route
	public MapPoint getStartPoint() {
		
		if(route.size()>0) {
			
			return route.get(0);
			
		} else {
			
			return new MapPoint();
		}
		
	}

	// stop is last on route
	public MapPoint getStopPoint() {
		
		if(route.size()>0) {
			
			return route.get(route.size() - 1);
			
		} else {
			
			return new MapPoint();
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
	private List<MapPoint> route = new ArrayList<MapPoint>();
	
	
	public List<MapPoint> getRoute() {
		return route;
	}

	public void setRoute(List<MapPoint> positions) {
		route.clear();
		for(MapPoint point:positions) {
			route.add(point);
		}
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
		route.add(point);
	}

	public byte[] getRouteAsBytes() {
		
		if(route!=null && route.size()>0) {
			
			List<byte[]> positions = new ArrayList<byte[]>();
			for(MapPoint point:route){
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
			route.add(new MapPoint(pos));
		}
	}
	

}
