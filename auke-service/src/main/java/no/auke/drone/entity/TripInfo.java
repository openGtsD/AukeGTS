package no.auke.drone.entity;

import com.sun.syndication.io.impl.Base64;

import no.auke.drone.annotation.Column;
import no.auke.drone.domain.Tracker.TrackerType;

// this is the storage class for trip
// and used for updates from UI
// later...

// This object is for store and retrieve som DB

public class TripInfo extends Trip {
	@Column
	private String trackerName;

    @Column
    private String owner;

    @Column
    private String contactInfo;

    @Column
    private TrackerType trackerType;

    @Column
    private String tripName;

    // where did the trip took place
    @Column
    private String country;

    @Column
    private String city;

    // what is this trip done (update by user)
    @Column
    private String description;

    // trip is to be shown in public
    // @Column TODO to check later
    private boolean isPublic;

    // this contain the compacted as bytearray route of trippoints
    // should be stored as a blob in database

    @Column
    private byte[] byteRoute;

    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public TrackerType getTrackerType() {
        return trackerType;
    }

    public void setTrackerType(TrackerType trackerType) {
        this.trackerType = trackerType;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

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
		
		
		byteRoute=trip.getRouteAsBytes();
		byteRoute = Base64.encode(byteRoute);
		
	}
	
	public Trip getTrip() {
		
		Trip trip = new Trip(getTrackerId(), getId());
		trip.setRouteAsBytes(Base64.decode(byteRoute));
		return trip;
	}
	
	

}
