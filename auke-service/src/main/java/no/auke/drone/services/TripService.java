package no.auke.drone.services;

import java.util.List;

import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.trips.TripInfo;

/**
 * Created by huyduong on 6/14/2015.
 */
public interface TripService {
    void saveTrip(Tracker tracker);
    List<TripInfo> getLatestTrips();
    void deleteTrip(TripInfo trip);
    List<TripInfo> getTripsByTrackerId(String trackerId);
    void deleteTripsByTrackerId(String trackerId);
    TripInfo getTripById(String tripId);
	void detectMoving(Tracker tracker);
}
