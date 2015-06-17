package no.auke.drone.services;

import junit.framework.Assert;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.dao.impl.CRUDDaoImpl;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.test.AbstractIntegrationTest;
import no.auke.drone.utils.PointUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * Created by huyduong on 6/16/2015.
 */
public class TripServiceTest extends AbstractIntegrationTest {
    @Autowired
    private TripService tripService;

    @Autowired
    private CRUDDao<MapPoint> mapPointCRUDDao;

    @Before
    public void init() {
        mapPointCRUDDao.setPersistentClass(MapPoint.class);
        mapPointCRUDDao.deleteAll();
    }

    @Test
    public void testCreateTrip() {
        MapPoint mapPoint = PointUtil.generateRandomMapPoint(new MapPoint());
        tripService.saveTrip(mapPoint);

        List<MapPoint> mapPoints = mapPointCRUDDao.getAll();
        Assert.assertEquals(1,mapPoints.size());
    }

    @Test
    public void testDeleteTrip() {
        MapPoint mapPoint = PointUtil.generateRandomMapPoint(new MapPoint());
        mapPoint.setId("1234");
        tripService.saveTrip(mapPoint);

        List<MapPoint> mapPoints = mapPointCRUDDao.getAll();
        Assert.assertEquals(1,mapPoints.size());

        tripService.deleteTrip(mapPoint);

        mapPoints = mapPointCRUDDao.getAll();
        Assert.assertEquals(0,mapPoints.size());
    }

    @Test
    public void testGetLatestTrips() {
        for(int i = 0; i < 10; i++) {
            MapPoint mapPoint = PointUtil.generateRandomMapPoint(new MapPoint());
            mapPoint.setId(UUID.randomUUID().toString());
            tripService.saveTrip(mapPoint);

        }

        List<MapPoint> mapPoints = mapPointCRUDDao.getAll();
        Assert.assertEquals(10,mapPoints.size());

        mapPoints = tripService.getLatestTrips();
        Assert.assertEquals(5,mapPoints.size());
    }

    @Test
    public void testGetTripsByTrackerId() {
        for(int i = 0; i < 10; i++) {
            MapPoint mapPoint = PointUtil.generateRandomMapPoint(new MapPoint());
            mapPoint.setId(UUID.randomUUID().toString());
            mapPoint.setTrackerId(String.valueOf(i));
            tripService.saveTrip(mapPoint);

            MapPoint mapPoint2 = PointUtil.generateRandomMapPoint(new MapPoint());
            mapPoint2.setId(UUID.randomUUID().toString());
            mapPoint2.setTrackerId(String.valueOf(i));
            tripService.saveTrip(mapPoint2);
        }

        List<MapPoint> mapPoints = mapPointCRUDDao.getAll();
        Assert.assertEquals(20,mapPoints.size());

        for(int i = 0; i < 10; i++) {
            mapPoints = tripService.getTripsByTrackerId(String.valueOf(i));
            Assert.assertEquals(2,mapPoints.size());
        }
    }

    @Test
    public void testDeleteTripsByTrackerId() {
        for(int i = 0; i < 10; i++) {
            MapPoint mapPoint = PointUtil.generateRandomMapPoint(new MapPoint());
            mapPoint.setId(UUID.randomUUID().toString());
            mapPoint.setTrackerId(String.valueOf(i));
            tripService.saveTrip(mapPoint);

            MapPoint mapPoint2 = PointUtil.generateRandomMapPoint(new MapPoint());
            mapPoint2.setId(UUID.randomUUID().toString());
            mapPoint2.setTrackerId(String.valueOf(i));
            tripService.saveTrip(mapPoint2);
        }

        List<MapPoint> mapPoints = mapPointCRUDDao.getAll();
        Assert.assertEquals(20,mapPoints.size());

        for(int i = 0; i < 10; i++) {
            tripService.deleteTripsByTrackerId(String.valueOf(i));
            mapPoints = tripService.getTripsByTrackerId(String.valueOf(i));
            Assert.assertEquals(0,mapPoints.size());
        }
    }
}
