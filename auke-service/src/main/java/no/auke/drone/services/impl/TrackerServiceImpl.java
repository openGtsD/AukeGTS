package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import no.auke.drone.application.impl.SimpleTrackerFactory;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.*;
import no.auke.drone.services.TrackerService;
import no.auke.drone.utils.PointUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huyduong on 3/25/2015.
 */

@Service
public class TrackerServiceImpl implements TrackerService {
    @Autowired
    private CRUDDao<Device> crudDeviceDao;

    @Autowired
    private SimpleTrackerFactory simpleTrackerFactory;

	private static final Logger logger = LoggerFactory.getLogger(TrackerServiceImpl.class);

    private static ExecutorService executor = Executors.newCachedThreadPool();
    public static ExecutorService getExecutor() {
        return executor;
    }	

    @PostConstruct
    public void initTrackerService() {
        crudDeviceDao.setPersistentClass(Device.class);
    	logger.info("initializing SIMULATED tracker services");
        
    	List<Tracker> trackers = new TrackerServiceFacade().createTrackersForCapitalCities();
        
        for (Tracker tracker : trackers) {
            TrackerData.getInstance().register((Observer) tracker);
        }
        logger.info("finished initializing SIMULATED tracker services");

        List<Device> devices = crudDeviceDao.getAll();
        if(CollectionUtils.isNotEmpty(devices)) {
            for(Device device : devices) {
                TrackerData.getInstance().register((Observer) simpleTrackerFactory.from(device) );
            }
        }

        TrackerData.getInstance().startCalculate();
    }

    public TrackerServiceImpl() {

    }

    @Override
    public Tracker registerTracker(String id, String name) {
        if(StringUtils.isEmpty(id)) {
            return null;
        }
        
        Tracker tracker = getTracker(id);
        if(tracker == null) {
            tracker = simpleTrackerFactory.create(id, name);
            TrackerData.getInstance().register((Observer) tracker);
            crudDeviceDao.create(new Device().from(tracker));
        }
        // else do no things. that's mean tracker exists in system
        return tracker;
    }

    @Override
    public Tracker removeTracker(String id) {
        
    	Tracker tracker = TrackerData.getInstance().getTracker(id);
    	if(tracker == null) {
    	   return null;
    	}
        crudDeviceDao.delete(new Device().from(tracker));
        TrackerData.getInstance().remove((Observer) tracker);
        return tracker;
    
    }

    @Override
    public Tracker getTracker(String id) {
    
    	Tracker tracker = TrackerData.getInstance().getTracker(id) != null ? (Tracker) TrackerData.getInstance().getTracker(id)
                : null;
        return tracker;
    
    }

    @Override
    public Collection<Tracker> getAll(String layerId) {
    	return TrackerData.getInstance().getTrackers(layerId);
    }

    @Override
    public Collection<Tracker> getAll() {
        return getAll(null);
    }

    @Override
    public Tracker move(String id, Integer speed, Integer course) {
    
    	Tracker tracker = TrackerData.getInstance().getTracker(id) != null ? (Tracker) TrackerData.getInstance().getTracker(id): null;
        
    	if (tracker != null) {
            tracker.move(speed,course);
        }
        
    	return tracker;
    
    }

    private class TrackerServiceFacade {
        
    	public List<Tracker> createTrackersForCapitalCities() {
            
        	List<Tracker> result = new ArrayList<Tracker>();
            List<MapPoint> points = new ArrayList<MapPoint>();
        
            points.add(new MapPoint(10.823099, 106.629664,0,0,0));// HCM
            points.add(new MapPoint(59.913869, 10.752245,0,0,0));// OSLO
            points.add(new MapPoint(55.378051, -3.435973,0,0,0));// UK
            points.add(new MapPoint(51.507351, -0.127758,0,0,0));// London
            points.add(new MapPoint(56.130366, -106.346771,0,0,0));// Canada

            for (int i = 0; i < points.size(); i++) {
                MapPoint point = points.get(i);
                for (int j = 1; j <= 10; j++) {
                    MapPoint rd = PointUtil.generateRandomMapPoint(point);
                    Tracker tracker = simpleTrackerFactory.create("SIMULATED",UUID.randomUUID().toString(), "Tracker" + i + "-"
                            + j, 2 * j, 2 * j, System.currentTimeMillis(), Tracker.TrackerType.SIMULATED, null, true, rd, "0123222" + i, "123123123" + j);
                    result.add(tracker);
                }
            }
            return result;
        }

    }

    //TODO: use function within trackerLayer
    @Override
    public Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom, String layerId) {
        
    	return new ArrayList<Tracker>(TrackerData.getInstance().getTrackerLayer(layerId).loadWithinView(boundary,zoom));
        
    };

    @Override
    public void calculateAll() {
    	
    	for(TrackerLayer layer:TrackerData.getInstance().getLayers()) {
    		
    		layer.calculateAll();
    		
    	}
        
    };
    
    @Override
    public Tracker start(String id) {
        
    	Tracker tracker = getTracker(id);
        if (tracker != null) {
        
        	tracker.setMoving(true);
            // setting the tracker altitude to 100, this is for testing simulation
            // only
            tracker.setAltitude(100);
            tracker.getCurrentPosition().setAltitude(100);
        
        }
        return tracker;

    }

    @Override
    public Tracker stop(String id) {
        
    	Tracker tracker = getTracker(id);
        
    	if (tracker != null) {
        
    		tracker.setMoving(false);
        
    		// setting tracker altitude to 0, this is for testing simulation only
            tracker.setAltitude(0);
            tracker.getCurrentPosition().setAltitude(0);
        }
    	
        return tracker;
    }

    @Override
    public Tracker update(Tracker tracker) {
        crudDeviceDao.update(new Device().from(tracker));
    	return TrackerData.getInstance().update(tracker);
    
    }

	@Override
	public void stopService() {
		
		getExecutor().shutdownNow();
		TrackerData.clear();
		
	}
}
