package no.auke.drone.domain.trips;

import no.auke.drone.domain.Tracker.TrackerType;

// this is the storage class for trip
// and used for updates from UI
// later...

// This object is for store and retrieve som DB

public class TripInfo extends Trip {
	
	private String trackerName;
    void setTrackerName(String trackerName) {
    	this.trackerName=trackerName;
    }
    String getTrackerName() {
    	return trackerName;
    }

	private String OwnerName;	
    public String getOwnerName() {
		return OwnerName;
	}
	public void setOwnerName(String ownerName) {
		OwnerName = ownerName;
	}

	private String contactInfo;
    void setContactInfo(String contactInfo) {
    	this.contactInfo=contactInfo;
    }

    String getContactInfo() {
    	return contactInfo;
    }

    TrackerType trackerType;
    void setTrackerType(TrackerType trackerType) {
    	
    	this.trackerType=trackerType;
    }
    TrackerType getTrackerType() {
    	return trackerType;
    }
	
	// we add more attributes to trip later
	// but the trip
	
	
	
	private String tripName;
	
	// where did the trip tok place
	private String country;
	private String city;
	
	
	// what is this trip done (update by user)
	private String description;
	
	// trip is to be shown in public
	private boolean ispublic;
	
	// this contain the compacted as bytearray route of trippoints
	// should be stored as a blob in database
	
	private byte[] byteRoute;
	
	public byte[] getByteRoute() {
		return byteRoute;
	}
	public void setByteRoute(byte[] byteRoute) {
		this.byteRoute = byteRoute;
	}
	
	// LHA: must be studied closer how to mock information in and out
	// but trip should store som tracker information so the trip migh be distributed 
	// to other systems without the tracker object
	
	
	
	public TripInfo(Trip trip){
		
		super(trip.getTracker());
		
		
		this.setTrackerId(trip.getTrackerId());
		this.setTrackerName(trip.getTracker().getName());
		//this.setOwnerName(trip.getTracker().getOwnerName());
		this.setContactInfo(trip.getTracker().getContactInfo());
		
		byteRoute=trip.getRouteAsBytes();
		
	}
	
	public Trip getTrip() {
		
		Trip trip = new Trip(getTrackerId(),getTripId());
		trip.setRouteAsBytes(byteRoute);
		
		return trip;
		
	}
	
	

}
