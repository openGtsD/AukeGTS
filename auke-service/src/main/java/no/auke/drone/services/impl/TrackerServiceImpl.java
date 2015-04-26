package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import no.auke.drone.dao.impl.SimpleTrackerFactory;
import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Observer;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerData;
import no.auke.drone.domain.TrackerLayer;
import no.auke.drone.services.TrackerService;
import no.auke.drone.utils.PointUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by huyduong on 3/25/2015.
 */

@Service
public class TrackerServiceImpl implements TrackerService {
    
	private static final Logger logger = LoggerFactory.getLogger(TrackerServiceImpl.class);

    private static ExecutorService executor = Executors.newCachedThreadPool();
    public static ExecutorService getExecutor() {
        return executor;
    }	

    @PostConstruct
    public void initTrackerService() {
        
    	logger.info("initializing tracker services");
        
    	List<Tracker> trackers = new TrackerServiceFacade().createTrackersForCapitalCities();
        
        for (Tracker tracker : trackers) {
            TrackerData.getInstance().register((Observer) tracker);
        }
        logger.info("finished initializing tracker services");
    
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
            tracker = new SimpleTrackerFactory().create(id, name);
            TrackerData.getInstance().register((Observer) tracker);
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
                    Tracker tracker = new SimpleTrackerFactory().create("SIMULATED",UUID.randomUUID().toString(), "Tracker" + i + "-"
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
        
    	return TrackerData.getInstance().update(tracker);
    
    }

	@Override
	public void stopService() {
		
		getExecutor().shutdownNow();
		TrackerData.clear();
		
	}
}
