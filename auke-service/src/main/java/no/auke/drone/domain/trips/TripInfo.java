package no.auke.drone.domain.trips;

// this is the storage class for trip
// and used for updates from UI
// later...

// This object is for store and retrieve som DB

public class TripInfo extends Trip {
	
	// we add more attributes to trip later
	// but the trip
	
	private String name;
	
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
	
	public TripInfo(Trip trip){
		
		super(trip.getTripId());
		this.setTrackerId(trip.getTrackerId());
		byteRoute=trip.getRouteAsBytes();
		
	}
	
	public Trip getTrip() {
		
		Trip trip = new Trip(getTrackerId(),getTripId());
		trip.setRouteAsBytes(byteRoute);
		
		return trip;
		
	}
	
	

}
