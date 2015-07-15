package no.auke.drone.services.impl;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import no.auke.drone.application.impl.SimpleTrackerFactory;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.*;
import no.auke.drone.domain.Observer;
import no.auke.drone.services.TrackerService;
import no.auke.drone.utils.PointUtil;
import no.auke.drone.utils.YmlPropertiesPersister;

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
    private CRUDDao<MapPoint> mapPointCRUDDao;

    @Autowired
    private SimpleTrackerFactory simpleTrackerFactory;
    
    @Autowired
    private YmlPropertiesPersister propertiesPersister;

	private static final Logger logger = LoggerFactory.getLogger(TrackerServiceImpl.class);

    private static ExecutorService executor = Executors.newCachedThreadPool();
    public static ExecutorService getExecutor() {
        return executor;
    }	

    @PostConstruct
    public void initTrackerService() {
        crudDeviceDao.setPersistentClass(Device.class);
        mapPointCRUDDao.setPersistentClass(MapPoint.class);

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

        mapPointCRUDDao.deleteAll();

        TrackerData.getInstance().startCalculate();
    }

    public TrackerServiceImpl() {

    }

    @Override
    public Tracker registerTracker(Tracker tracker) {
        // return TrackerData.getInstance().register((Observer) tracker);
        Tracker result = tracker;

        if(tracker == null || StringUtils.isEmpty(tracker.getId())) {
            return null;
        }

        Tracker persistedTracker = getTracker(tracker.getId());
        if(persistedTracker == null) {
            persistedTracker = simpleTrackerFactory.create(tracker);
            TrackerData.getInstance().register((Observer) persistedTracker );
            crudDeviceDao.create(new Device().from(persistedTracker));
            result = persistedTracker;
        }

        // else do no things. that's mean tracker exists in system
        return result;
    }

    @Override
    public Tracker registerTracker(String id, String name) {
        return registerTracker(new SimpleTrackerFactory().create(id,name));
    }

    @Override
    public Collection<Tracker> removeAll(String layerId) {
        Collection<Tracker> trackers = getAll(layerId);
        for(Tracker tracker : trackers) {
            remove(tracker.getId());
        }
        return trackers;
    }

    @Override
    public Collection<Tracker> removeAll() {
        return removeAll(null);
    }

    @Override
    public Tracker remove(String trackerId) {
        
    	Tracker tracker = TrackerData.getInstance().getTracker(trackerId);
    	if(tracker == null) {
    	   return null;
    	}
        crudDeviceDao.delete(new Device().from(tracker));
        TrackerData.getInstance().remove((Observer) tracker);
        return tracker;
    
    }

    @Override
    public Tracker getTracker(String id, boolean refresh) {
        Tracker tracker = null;
        if(!refresh) {
            tracker = getTracker(id);
        }

        if(refresh || tracker == null) {
            Device device = crudDeviceDao.getById(id);
            tracker = simpleTrackerFactory.from(device);

            if(tracker != null) {
                TrackerData.getInstance().register((Observer) tracker);
            }
        }

        return tracker;

    }

    @Override
    public Tracker getTracker(String id) {
    	Tracker tracker = TrackerData.getInstance().getTracker(id) != null ? (Tracker) TrackerData.getInstance().getTracker(id)
                : null;
        return tracker;
    
    }

    @Override
    public TrackerLayer getTrackerLayer(String layerId) {
        return TrackerData.getInstance().getTrackerLayer(layerId);
    }

    @Override
    public Collection<TrackerLayer> getTrackerLayers() {
        return TrackerData.getInstance().getLayers();
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
    public Collection<Tracker> getTrackersByIds(List<String> ids) {
        List<Tracker> trackers = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(ids)) {
            for(Tracker tracker : getAll()) {
                for(String id : ids) {
                    if(tracker.getId().equalsIgnoreCase(id)) {
                        trackers.add(tracker);
                    }
                }
            }
        }

        return trackers;
    }

    @Override
    public Collection<Tracker> getLatestRegisteredTrackers(String trackerLayer) {
        Collection<Tracker> trackers = new LinkedList<>();
        Object[] trackerArray = TrackerData.getInstance().getTrackerLayer(trackerLayer).getActiveTrackers().toArray();
        
        // THAI - better we shuold  make query base on create date field into openGTS ?? its root cause impact to performance
        for(int i = trackerArray.length - 1; i >= 0 && trackers.size() < propertiesPersister.getNumberByKey("tracker.maxSize"); i--) {
            trackers.add((SimpleTracker) trackerArray[i]);
        }
        
        

        return trackers;
    }

    @Override
    public Collection<Tracker> getLongestFlightTrackers(String trackerLayer) {
        Collection<Tracker> trackers = new LinkedList<>();
        Iterator<Tracker> iterator = TrackerData.getInstance().getTrackerLayer(trackerLayer).getActiveTrackers().iterator();

        // THAI - better we shuold  make query base on time field into openGTS ??. its root cause impact to performance
        while(iterator.hasNext() && trackers.size() < propertiesPersister.getNumberByKey("tracker.maxSize")) {
            trackers.add(iterator.next());
        }

        return trackers;
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
            points.add(new MapPoint(51.507351, -0.127758,0,0,0));// London
            points.add(new MapPoint(56.130366, -106.346771,0,0,0));// Canada

            for (int i = 0; i < points.size(); i++) {
                
            	MapPoint point = points.get(i);
                
            	for (int j = 1; j <= 10; j++) {
                
            		MapPoint rd = PointUtil.generateRandomMapPoint(point);
                    Tracker tracker = simpleTrackerFactory.create("SIMULATED",UUID.randomUUID().toString(), "Tracker" + i + "-"
                            + j, Tracker.TrackerType.SIMULATED, null,  rd, "0123222" + i, "123123123" + j);
                    result.add(tracker);
                }
            }

            //create 2 trackers for Stig and LHF
            MapPoint stig = new MapPoint(59.744076, 10.204455,0,0,0); // Stig
            Tracker stigTracker = simpleTrackerFactory.create("SIMULATED",UUID.randomUUID().toString(), "Tracker" + "Stig" + "-"
                    + "Stig", Tracker.TrackerType.SIMULATED, null, stig, "0123222" + 1, "123123123" + 1);
            result.add(stigTracker);
            
            // THAI - add more data for test
            for (int k = 1; k <= 50; k++) {
                MapPoint rd = PointUtil.generateRandomMapPoint(stig);
                Tracker tracker = simpleTrackerFactory.create("SIMULATED",UUID.randomUUID().toString(), "Tracker" + "Stig" + "-"
                        + "Stig" + k, Tracker.TrackerType.SIMULATED, null, rd, "0123222" + 1, "123123123" + 1);
                result.add(tracker);
            }

            MapPoint lhf = new MapPoint(59.722268, 10.213038,0,0,0);
            Tracker lhfTracker = simpleTrackerFactory.create("SIMULATED",UUID.randomUUID().toString(), "Tracker" + "LHF" + "-"
                    + "LHF", Tracker.TrackerType.SIMULATED, null, lhf, "0123222" + 1, "123123123" + 1);
            result.add(lhfTracker);
            
            // THAI - add more data for test
            for (int l = 1; l <= 50; l++) {
                MapPoint rd = PointUtil.generateRandomMapPoint(lhf);
                Tracker tracker = simpleTrackerFactory.create("SIMULATED",UUID.randomUUID().toString(), "Tracker" + "LHF" + "-"
                        + "Stig" + l, Tracker.TrackerType.SIMULATED, null, rd, "0123222" + 1, "123123123" + 1);
                result.add(tracker);
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
    public void updateActiveTracker(Tracker tracker) {
        TrackerData.getInstance().getTrackerLayer(tracker.getLayerId()).updateActiveTracker(tracker);
    }

    @Override
	public void stopService() {
		
		getExecutor().shutdownNow();
		TrackerData.clear();
		
	}

    @Override
    public Collection<Tracker> getActiveTrackers() {
        return getActiveTrackers(null);
//        return new ArrayList<>(TrackerData.getInstance().getActiveTrackers()).subList(0, propertiesPersister.getNumberByKey("track.maxActive"));
    }

    @Override
    public Collection<Tracker> getActiveTrackers(String layerId) {
        return new ArrayList<>(TrackerData.getInstance().getActiveTrackers(layerId));
    }

    @Override
    public Collection<Tracker> getPassiveTrackers() {
        return getPassiveTrackers(null);
    }

    @Override
    public Collection<Tracker> getPassiveTrackers(String layerId) {
        return new ArrayList<>(TrackerData.getInstance().getPassiveTrackers(layerId));
    }
}
