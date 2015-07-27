package no.auke.drone.services;

import no.auke.drone.domain.AbstractTrackerBase;
import no.auke.drone.domain.trips.TripInfo;

import java.util.List;

/**
 * Created by huyduong on 6/14/2015.
 */
public interface TripService {
    void saveTrip(AbstractTrackerBase tracker);
    List<TripInfo> getLatestTrips();
    void deleteTrip(TripInfo trip);
    List<TripInfo> getTripsByTrackerId(String trackerId);
    void deleteTripsByTrackerId(String trackerId);
    TripInfo getTripById(String tripId);
}
