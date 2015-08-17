package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import no.auke.drone.application.impl.SimpleTrackerFactory;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Observer;
import no.auke.drone.domain.SimpleTracker;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerData;
import no.auke.drone.entity.Device;
import no.auke.drone.entity.TrackerDB;
import no.auke.drone.entity.TripInfo;
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
    private CRUDDao<Device> deviceDao;

    @Autowired
    private CRUDDao<TrackerDB> trackerDao;

    @Autowired
    private CRUDDao<MapPoint> mapPointDao;
    
    @Autowired
    private CRUDDao<TripInfo> tripDao;

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
        
    	deviceDao.setPersistentClass(Device.class);
        trackerDao.setPersistentClass(TrackerDB.class);
        mapPointDao.setPersistentClass(MapPoint.class);
        tripDao.setPersistentClass(TripInfo.class);
        

        logger.info("initializing SIMULATED tracker services");

        //THAI: we need consider should be store all history for a tracker ???
        tripDao.deleteAll();
        
        List<Tracker> trackers = new TrackerServiceFacade().createTrackersForCapitalCities();

        for (Tracker tracker : trackers) {
            TrackerData.getInstance().register((Observer) tracker);
        }
        logger.info("finished initializing SIMULATED tracker services");

        List<Device> devices = deviceDao.getAll();
        if (CollectionUtils.isNotEmpty(devices)) {
            for (Device device : devices) {
                TrackerData.getInstance().register((Observer) simpleTrackerFactory.from(device));
            }
        }

        List<TrackerDB> trackerDBs = trackerDao.getAll();
        if (CollectionUtils.isNotEmpty(trackerDBs)) {
            for (TrackerDB trackerDB : trackerDBs) {
                TrackerData.getInstance().register((Observer) simpleTrackerFactory.from(trackerDB));
            }
        }

        
        

        mapPointDao.deleteAll();
        
        
//      THAI: why we call this method again ?? Since its ready run at time register tracker
//        TrackerData.getInstance().startCalculate();
    }

    public TrackerServiceImpl() {

    }

    @Override
    public Tracker registerTracker(Tracker tracker, boolean persist) {
        Tracker result = tracker;

        if (tracker == null || StringUtils.isEmpty(tracker.getId())) {
            return null;
        }

        Tracker persistedTracker = getTracker(tracker.getId());
        if (persistedTracker == null) {
            persistedTracker = simpleTrackerFactory.create(tracker);
            TrackerData.getInstance().register((Observer) persistedTracker);
            if (persist) {
                deviceDao.create(new Device().from(persistedTracker));
                trackerDao.create(new TrackerDB().from(persistedTracker));
            }
            result = persistedTracker;
        }

        // else do no things. that's mean tracker exists in system
        return result;
    }

    @Override
    public Tracker registerTracker(Tracker tracker) {
        return registerTracker(tracker, true);
    }

    @Override
    public Collection<Tracker> removeAll(String layerId) {
        Collection<Tracker> trackers = getAll(layerId);
        for (Tracker tracker : trackers) {
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
        if (tracker == null) {
            return null;
        }
        deviceDao.delete(new Device().from(tracker));
        trackerDao.delete(new TrackerDB().from(tracker));
        TrackerData.getInstance().remove((Observer) tracker);
        return tracker;

    }

    @Override
    public Tracker getTracker(String id, boolean refresh) {
        Tracker tracker = null;
        if (!refresh) {
            tracker = getTracker(id);
        }

        if (refresh || tracker == null) {
            Device device = deviceDao.getById(id);
            tracker = simpleTrackerFactory.from(device);

            if (tracker != null) {
                TrackerData.getInstance().register((Observer) tracker);
            }
        }

        return tracker;

    }

    @Override
    public Tracker getTracker(String id) {
        Tracker tracker = null;
        TrackerDB trackerDb = trackerDao.getById(id);
        if(null != trackerDb) {
            tracker = simpleTrackerFactory.from(trackerDb);
        } else {//THAI: for Simulate drones not insert DB. We need persist at init ??
            tracker = TrackerData.getInstance().getTracker(id) != null ? (Tracker) TrackerData.getInstance()
                    .getTracker(id) : null;
        }
        return tracker;

    }

    @Override
    public LayerServiceImpl getTrackerLayer(String layerId) {
        return TrackerData.getInstance().getTrackerLayer(layerId);
    }

    @Override
    public Collection<LayerServiceImpl> getTrackerLayers() {
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
        if (CollectionUtils.isNotEmpty(ids)) {
            for (Tracker tracker : getAll()) {
                for (String id : ids) {
                    if (tracker.getId().equalsIgnoreCase(id)) {
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

        // THAI - better we shuold make query base on create date field into
        // openGTS ?? its root cause impact to performance
        for (int i = trackerArray.length - 1; i >= 0
                && trackers.size() < propertiesPersister.getNumberByKey("tracker.maxSize"); i--) {
            trackers.add((SimpleTracker) trackerArray[i]);
        }

        return trackers;
    }

    @Override
    public Collection<Tracker> getLongestFlightTrackers(String trackerLayer) {
        Collection<Tracker> trackers = new LinkedList<>();
        Iterator<Tracker> iterator = TrackerData.getInstance().getTrackerLayer(trackerLayer).getActiveTrackers()
                .iterator();

        // THAI - better we shuold make query base on time field into openGTS
        // ??. its root cause impact to performance
        while (iterator.hasNext() && trackers.size() < propertiesPersister.getNumberByKey("tracker.maxSize")) {
            trackers.add(iterator.next());
        }

        return trackers;
    }

    @Override
    public Tracker move(String id, Integer speed, Integer course) {

        Tracker tracker = TrackerData.getInstance().getTracker(id) != null ? (Tracker) TrackerData.getInstance()
                .getTracker(id) : null;

        if (tracker != null) {
            tracker.move(speed, course);
        }

        return tracker;

    }

    private class TrackerServiceFacade {

        public List<Tracker> createTrackersForCapitalCities() {

            List<Tracker> result = new ArrayList<Tracker>();
            List<MapPoint> points = new ArrayList<MapPoint>();

            points.add(new MapPoint(10.823099, 106.629664, 0, 0, 0));// HCM
            points.add(new MapPoint(59.913869, 10.752245, 0, 0, 0));// OSLO
            points.add(new MapPoint(51.507351, -0.127758, 0, 0, 0));// London
            points.add(new MapPoint(56.130366, -106.346771, 0, 0, 0));// Canada

            for (int i = 0; i < points.size(); i++) {

                MapPoint point = points.get(i);

                for (int j = 1; j <= 10; j++) {

                    MapPoint rd = PointUtil.generateRandomMapPoint(point);
                    Tracker tracker = simpleTrackerFactory.create("SIMULATED", UUID.randomUUID().toString(), "Tracker"
                            + i + "-" + j, "", Tracker.TrackerType.SIMULATED, "Tracker"
                                    + i + "-" + j, rd, "0123222" + i, "123123123" + j, "", true);
                    result.add(tracker);
                }
            }

            // create 2 trackers for Stig and LHF
            MapPoint stig = new MapPoint(59.744076, 10.204455, 50, 40, 30); // Stig
            Tracker stigTracker = simpleTrackerFactory.create("SIMULATED", "1", "Tracker"
                    + "Stig" + "-" + "Stig", "", Tracker.TrackerType.SIMULATED, "Stig", stig, "0123222" + 1,
                    "123123123" + 1, "ContactInfo", true);
            result.add(stigTracker);

//            // THAI - add more data for test
            for (int k = 1; k <= 50; k++) {
                MapPoint rd = PointUtil.generateRandomMapPoint(stig);
                Tracker tracker = simpleTrackerFactory.create("SIMULATED", UUID.randomUUID().toString(), "Tracker"
                        + "Stig" + "-" + "Stig" + k, "", Tracker.TrackerType.SIMULATED, null, rd, "0123222" + 1,
                        "123123123" + 1, "", true);
                result.add(tracker);
            }

            MapPoint lhf = new MapPoint(59.722268, 10.213038, 0, 0, 0);
            Tracker lhfTracker = simpleTrackerFactory
                    .create("SIMULATED", UUID.randomUUID().toString(), "Tracker" + "LHF" + "-" + "LHF", "",
                            Tracker.TrackerType.SIMULATED, null, lhf, "0123222" + 1, "123123123" + 1, "", true);
            result.add(lhfTracker);

            // THAI - add more data for test
            for (int l = 1; l <= 50; l++) {
                MapPoint rd = PointUtil.generateRandomMapPoint(lhf);
                Tracker tracker = simpleTrackerFactory.create("SIMULATED", UUID.randomUUID().toString(), "Tracker"
                        + "LHF" + "-" + "Stig" + l, "", Tracker.TrackerType.SIMULATED, null, rd, "0123222" + 1,
                        "123123123" + 1, "", true);
                result.add(tracker);
            }

            return result;
        }

    }

    // TODO: use function within trackerLayer
    @Override
    public Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom, String layerId) {

        return new ArrayList<Tracker>(TrackerData.getInstance().getTrackerLayer(layerId).loadWithinView(boundary, zoom));

    };

    @Override
    public void calculateAll() {

        for (LayerServiceImpl layer : TrackerData.getInstance().getLayers()) {

            layer.calculateAll();

        }

    };

    @Override
    public Tracker start(String id) {

        Tracker tracker = getTracker(id);
        if (tracker != null) {

            tracker.setMoving(true);
            // setting the tracker altitude to 100, this is for testing
            // simulation
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

            // setting tracker altitude to 0, this is for testing simulation
            // only
            tracker.getCurrentPosition().setAltitude(0);
        }

        return tracker;
    }

    @Override
    public Tracker update(Tracker tracker) {
        deviceDao.update(new Device().from(tracker));
        trackerDao.update(new TrackerDB().from(tracker));
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
        // return new
        // ArrayList<>(TrackerData.getInstance().getActiveTrackers()).subList(0,
        // propertiesPersister.getNumberByKey("track.maxActive"));
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
