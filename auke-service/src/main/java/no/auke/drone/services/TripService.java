package no.auke.drone.services;

import no.auke.drone.domain.MapPoint;

import java.util.List;

/**
 * Created by huyduong on 6/14/2015.
 */
public interface TripService {
    MapPoint saveTrip(MapPoint mapPoint);
    List<MapPoint> getLatestTrips();
    void deleteTrip(MapPoint mapPoint);
    List<MapPoint> getTripsByTrackerId(String trackerId);
    void deleteTripsByTrackerId(String trackerId);
    MapPoint getTripById(String tripId);
}
