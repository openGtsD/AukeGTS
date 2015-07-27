package no.auke.drone.services.impl;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.dao.QueryBuilder;
import no.auke.drone.domain.AbstractTrackerBase;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.services.TripService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.management.Query;

import java.util.List;
import java.util.Properties;

/**
 * Created by huyduong on 6/14/2015.
 */
@Service
public class TripServiceImpl implements TripService {
    private final int NUMB_LATEST_TRIP = 5;
    @Autowired
    private CRUDDao<MapPoint> mapPointCRUDDao;

    @PostConstruct
    public void init() {
        mapPointCRUDDao.setPersistentClass(MapPoint.class);
    }

    @Override
    public void saveTrip(AbstractTrackerBase tracker) {
    	
    	// Implement here 
    	
    	
        // return mapPointCRUDDao.create(mapPoint);
    }

    @Override
    public MapPoint getTripById(String tripId) {
        return mapPointCRUDDao.getById(tripId);
    }

    @Override
    public List<MapPoint> getLatestTrips() {
        return mapPointCRUDDao.getTop(NUMB_LATEST_TRIP);
    }

    @Override
    public void deleteTrip(MapPoint mapPoint) {
        mapPointCRUDDao.delete(mapPoint);
    }

    @Override
    public List<MapPoint> getTripsByTrackerId(String trackerId) {
        Properties properties = new Properties();
        properties.put("trackerId",trackerId);
        return mapPointCRUDDao.getByProperties(properties);
    }

    @Override
    public void deleteTripsByTrackerId(String trackerId) {
        Properties properties = new Properties();
        properties.put("trackerId",trackerId);
        mapPointCRUDDao.deleteAllByProperties(properties);
    }
}
