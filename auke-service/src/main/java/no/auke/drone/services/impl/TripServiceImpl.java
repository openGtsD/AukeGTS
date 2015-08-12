package no.auke.drone.services.impl;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.Tracker;
import no.auke.drone.entity.Trip;
import no.auke.drone.entity.TripInfo;
import no.auke.drone.services.TripService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void saveTrip(Tracker tracker) {
    	Trip trip = new Trip(tracker);
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

	@Override
	public void detectMoving(Tracker tracker) {
		
		// LHA: 
		
		// here we figure if tracker is moving or not by analysing
		// the positions 
		// for ex. if long time since last position (for ex. a minute) 
		// tracker is regarded stopped
		// Or if same position for some time 
		// or speed = 0 for some time
		
		// this logic we need to discuss
		
		if (false) {
			
			// this will also trigger a save on last trip 
			tracker.setMoving(false);
			
		}
		
		
		
		
		
		
	}
}
