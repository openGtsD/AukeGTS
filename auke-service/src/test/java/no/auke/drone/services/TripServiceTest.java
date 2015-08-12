package no.auke.drone.services;

import java.util.List;

import junit.framework.Assert;
import no.auke.drone.application.impl.SimpleTrackerFactory;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.AbstractIntegrationTest;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.SimpleTracker;
import no.auke.drone.domain.Tracker;
import no.auke.drone.entity.TripInfo;
import no.auke.drone.utils.PointUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huyduong on 6/16/2015.
 */
public class TripServiceTest extends AbstractIntegrationTest {
    @Autowired
    private TripService tripService;

    @Autowired
    private CRUDDao<TripInfo> tripInfoCRUDDao;

    @Autowired
    private SimpleTrackerFactory simpleTrackerFactory;

    private final String TRACKER_ID = "trackerID";
    private final String TRACKER_NAME = "trackerName";
    private Tracker simpleTracker;

    @Before
    public void init() {
        tripInfoCRUDDao.setPersistentClass(TripInfo.class);
        tripInfoCRUDDao.deleteAll();
        simpleTracker = new SimpleTracker(TRACKER_ID, TRACKER_NAME);
    }

    @After
    public void tearDown() {
        tripInfoCRUDDao.deleteAll();
    }

    @Test
    public void testCreateTrip() {
        
        Tracker tracker = simpleTrackerFactory.create(simpleTracker);
        tracker.setActive(true);

        MapPoint mapPoint1 = PointUtil.generateRandomMapPoint(new MapPoint());
        MapPoint mapPoint2 = PointUtil.generateRandomMapPoint(new MapPoint());

        tracker.getPositions().add(mapPoint1);
        tracker.getPositions().add(mapPoint2);

        tripService.saveTrip(tracker);

        List<TripInfo> tripInfos = tripInfoCRUDDao.getAll();
        Assert.assertEquals(1,tripInfos.size());
    }

    @Test
    public void testDeleteTrip() {
        Tracker tracker = simpleTrackerFactory.create(simpleTracker);
        tracker.setActive(true);

        MapPoint mapPoint1 = PointUtil.generateRandomMapPoint(new MapPoint());
        MapPoint mapPoint2 = PointUtil.generateRandomMapPoint(new MapPoint());

        tracker.getPositions().add(mapPoint1);
        tracker.getPositions().add(mapPoint2);

        tripService.saveTrip(tracker);

        List<TripInfo> tripInfos = tripInfoCRUDDao.getAll();
        Assert.assertEquals(1,tripInfos.size());

        tripService.deleteTrip(tripInfos.get(0));

        tripInfos = tripInfoCRUDDao.getAll();
//        Assert.assertEquals(0,tripInfos.size());
    }

    @Test
    public void testDeleteTripByTrackerId() {
        Tracker tracker = simpleTrackerFactory.create(simpleTracker);
        tracker.setActive(true);

        MapPoint mapPoint1 = PointUtil.generateRandomMapPoint(new MapPoint());
        MapPoint mapPoint2 = PointUtil.generateRandomMapPoint(new MapPoint());

        tracker.getPositions().add(mapPoint1);
        tracker.getPositions().add(mapPoint2);

        tripService.saveTrip(tracker);

        List<TripInfo> tripInfos = tripInfoCRUDDao.getAll();
        Assert.assertEquals(1,tripInfos.size());

        tripService.deleteTripsByTrackerId(TRACKER_ID);

        tripInfos = tripInfoCRUDDao.getAll();
        Assert.assertEquals(0,tripInfos.size());
    }

    @Test
    public void testGetLatestTrips() {

        
    }

    @Test
    public void testGetTripsByTrackerId() {

        
    }

    @Test
    public void testDeleteTripsByTrackerId() {

    }
}
