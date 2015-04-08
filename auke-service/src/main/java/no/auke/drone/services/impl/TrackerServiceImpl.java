package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import no.auke.drone.dao.impl.SimpleTrackerFactory;
import no.auke.drone.domain.*;
import no.auke.drone.domain.Tracker;
import no.auke.drone.services.TrackerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.auke.drone.ws.util.PointUtil;

/**
 * Created by huyduong on 3/25/2015.
 */
@Service
public class TrackerServiceImpl implements TrackerService {
    private static final Logger logger = LoggerFactory.getLogger(TrackerServiceImpl.class);

    Map<String, Tracker> trackers = new ConcurrentHashMap<String, Tracker>();

    @PostConstruct
    public void initTrackerService() {
        logger.info("initializing tracker services");
        List<Tracker> trackers = new TrackerServiceFacade().createTrackersForCapitalCities();
        for (Tracker tracker : trackers) {
            TrackerData.getInstance().register((Observer) tracker);
        }
        logger.info("finished initializing tracker services");
    }

    public TrackerServiceImpl() {

    }

    @Override
    public Tracker registerTracker(String id, String name) {
        Tracker tracker = new SimpleTrackerFactory().create(id, name);
        TrackerData.getInstance().register((Observer) tracker);
        return tracker;
    }

    @Override
    public Tracker removeTracker(String id) {
        Tracker tracker = new SimpleTrackerFactory().create(id, "");
        TrackerData.getInstance().remove((Observer) tracker);
        return tracker;
    }

    @Override
    public Tracker getTracker(String id) {
        Tracker tracker = TrackerData.getInstance().getDrone(id) != null ? (Tracker) TrackerData.getInstance().getDrone(id)
                : null;
        return tracker;
    }

    @Override
    public Collection<Tracker> getAll() {
        return TrackerData.getInstance().getTrackers().values();
    }

    @Override
    public Tracker move(String id, Integer speed, Integer course) {
        Tracker tracker = TrackerData.getInstance().getDrone(id) != null ? (Tracker) TrackerData.getInstance().getDrone(id)
                : null;
        if (tracker != null) {
            tracker.move(speed,course);
        }
        return tracker;
    }

    private class TrackerServiceFacade {
        public List<Tracker> createTrackersForCapitalCities() {
            List<Tracker> result = new ArrayList<Tracker>();
            List<MapPoint> points = new ArrayList<MapPoint>();
            points.add(new MapPoint(10.823099, 106.629664));// HCM
            points.add(new MapPoint(59.913869, 10.752245));// OSLO
            points.add(new MapPoint(55.378051, -3.435973));// UK
            points.add(new MapPoint(51.507351, -0.127758));// London
            points.add(new MapPoint(56.130366, -106.346771));// Canada

            for (int i = 0; i < points.size(); i++) {
                MapPoint point = points.get(i);
                for (int j = 1; j <= 10; j++) {
                    MapPoint rd = PointUtil.generateRandomMapPoint(point);
                    Tracker tracker = new SimpleTrackerFactory().create(UUID.randomUUID().toString(), "Tracker" + i + "-"
                            + j, 2 * j, 2 * j, System.currentTimeMillis(), "type", null, true, rd);
                    result.add(tracker);
                }
            }
            return result;
        }

    }

    @Override
    public List<Tracker> loadWithinView(BoundingBox boundary) {
        List<Tracker> result = new ArrayList<Tracker>();
        for (Tracker positionUnit : getAll()) {
            if (positionUnit.withinView(boundary.getSouthWestLat(), boundary.getSouthWestLon(),
                    boundary.getNorthEastLat(), boundary.getNorthEastLon())) {
                result.add(positionUnit);
            }
        }
        return result;
    }

    @Override
    public Tracker start(String id) {
        Tracker tracker = getTracker(id);
        if (tracker != null) {
            tracker.setFlying(true);
            // setting the tracker altitude to 100, this is for testing simulation
            // only
            tracker.setAltitude(100);
        }
        return tracker;
    }

    @Override
    public Tracker stop(String id) {
        Tracker tracker = getTracker(id);
        if (tracker != null) {
            tracker.setFlying(false);
            // setting tracker altitude to 0, this is for testing simulation only
            tracker.setAltitude(0);
        }
        return tracker;
    }
}
