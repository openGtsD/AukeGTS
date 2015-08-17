package no.auke.drone.entity;

import java.io.Serializable;

import com.sun.syndication.io.impl.Base64;

import no.auke.drone.annotation.Column;
import no.auke.drone.domain.Tracker.TrackerType;

// this is the storage class for trip
// and used for updates from UI
// later...

// This object is for store and retrieve som DB

public class TripInfo extends Trip implements Serializable {
    private static final long serialVersionUID = 2281073816884185202L;



    // this contain the compacted as bytearray route of trippoints
    // should be stored as a blob in database

    @Column
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

    public TripInfo(){};

	public TripInfo(Trip trip){
		
		super(trip.getTracker());
		this.setTrackerId(trip.getTrackerId());
		this.setTrackerName(trip.getTracker().getName());
		this.setOwner(trip.getTracker().getOwner());
		this.setContactInfo(trip.getTracker().getContactInfo());
		setByteRoute(trip.getRouteAsBytes());
		
//		THAI: temp comment out, maybe we will use later??
//		byteRoute = Base64.encode(byteRoute);
		
	}
	
	public Trip getTrip() {
		
		Trip trip = new Trip(getTrackerId(), getId());
		trip.setRouteAsBytes(byteRoute);
		return trip;
	}
	
	

}
