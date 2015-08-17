package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.Tracker;
import no.auke.drone.entity.Trip;
import no.auke.drone.entity.TripInfo;
import no.auke.drone.services.PositionCalculatorService;
import no.auke.drone.services.TripService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huyduong on 6/14/2015.
 */
@Service
public class TripServiceImpl implements TripService {
	
	private static final Logger logger = LoggerFactory.getLogger(TripServiceImpl.class);
    
	private final int NUMB_LATEST_TRIP = 5;

    @Autowired
    private CRUDDao<TripInfo> tripCRUDDao;

    @PostConstruct
    public void init() {

        tripCRUDDao.setPersistentClass(TripInfo.class);

    }

    @Override
    public void saveTrip(Tracker tracker) {
    	
		if(logger.isDebugEnabled())
            logger.debug("Save trip, trackerid " + tracker.getId() + " trip postions " + tracker.getPositions().size());
    	
        Trip trip = new Trip(tracker);
        tripCRUDDao.create(new TripInfo(trip));
        tracker.getPositions().clear();
    }

    @Override
    public Trip getTripById(String tripId) {
    	
        return tripCRUDDao.getById(tripId).getTrip();
    }

    @Override
    public List<Trip> getLatestTrips() {
    	
        List<Trip> list = new ArrayList<Trip>();
        for(TripInfo info:(List<TripInfo>)tripCRUDDao.getTop(NUMB_LATEST_TRIP)) {
        	list.add(info.getTrip());
        }
        return list;    	
    }

    @Override
    public void deleteTrip(Trip trip) {
        tripCRUDDao.delete(new TripInfo(trip));
    }

    @Override
    public List<Trip> getTripsByTrackerId(String trackerId) {
        Properties properties = new Properties();
        properties.put("trackerId", trackerId);
        
        List<Trip> list = new ArrayList<Trip>();
        for(TripInfo info:(List<TripInfo>)tripCRUDDao.getByProperties(properties)) {
        	list.add(info.getTrip());
        }
        return list;
    }

    @Override
    public void deleteTripsByTrackerId(String trackerId) {
        Properties properties = new Properties();
        properties.put("trackerId", trackerId);
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
