package no.auke.drone.services.impl;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.AbstractTrackerBase;
import no.auke.drone.domain.trips.Trip;
import no.auke.drone.domain.trips.TripInfo;
import no.auke.drone.services.TripService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.Properties;

/**
 * Created by huyduong on 6/14/2015.
 */
@Service
public class TripServiceImpl implements TripService {
    private final int NUMB_LATEST_TRIP = 5;
    
    @Autowired
    private CRUDDao<TripInfo> tripCRUDDao;

    @PostConstruct
    public void init() {
    
    	tripCRUDDao.setPersistentClass(TripInfo.class);
    
    }

    @Override
    public void saveTrip(AbstractTrackerBase tracker) {
    	
    	Trip trip = new Trip(tracker.getId());
    	trip.setRoute(tracker.getPositions());
    	tripCRUDDao.create(new TripInfo(trip));
    }

    @Override
    public TripInfo getTripById(String tripId) {
        return tripCRUDDao.getById(tripId);
    }

    @Override
    public List<TripInfo> getLatestTrips() {
        return tripCRUDDao.getTop(NUMB_LATEST_TRIP);
    }

    @Override
    public void deleteTrip(TripInfo trip) {
    	tripCRUDDao.delete(trip);
    }

    @Override
    public List<TripInfo> getTripsByTrackerId(String trackerId) {
        Properties properties = new Properties();
        properties.put("trackerId",trackerId);
        return tripCRUDDao.getByProperties(properties);
    }

    @Override
    public void deleteTripsByTrackerId(String trackerId) {
        Properties properties = new Properties();
        properties.put("trackerId",trackerId);
        tripCRUDDao.deleteAllByProperties(properties);
    }
}
