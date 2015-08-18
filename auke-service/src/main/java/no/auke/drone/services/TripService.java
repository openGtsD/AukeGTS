package no.auke.drone.services;

import java.util.List;

import no.auke.drone.domain.Tracker;
import no.auke.drone.entity.Trip;

/**
 * Created by huyduong on 6/14/2015.
 */
public interface TripService {
    void saveTrip(Tracker tracker);
    List<Trip> getLatestTrips();
    void deleteTrip(Trip trip);
    List<Trip> getTripsByTrackerId(String trackerId);
    void deleteTripsByTrackerId(String trackerId);
    Trip getTripById(String tripId);
	void detectMoving(Tracker tracker);
}
