package no.auke.drone.domain.test;

import junit.framework.Assert;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.*;
import no.auke.drone.services.TrackerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Properties;

/**
 * Created by huyduong on 5/12/2015.
 */
public class TrackerServiceIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    TrackerService trackerService;

    @Autowired
    CRUDDao<Device> deviceCRUDDao;

    @Autowired
    CRUDDao<EventData> eventDataCRUDDao;

    @Before
    public void init() {
        deviceCRUDDao.setPersistentClass(Device.class);
        eventDataCRUDDao.setPersistentClass(EventData.class);

        deviceCRUDDao.deleteAll();
        eventDataCRUDDao.deleteAll();
    }

    @After
    public void tearDown() {
        trackerService.removeAll();
        deviceCRUDDao.deleteAll();
        eventDataCRUDDao.deleteAll();
    }

    @Test
    public void shouldRegisterNewDrone() {
        String newTrackerId = "11223344";
        trackerService.registerTracker(newTrackerId, "this is a new name");
        Tracker tracker = trackerService.getTracker(newTrackerId);
        Assert.assertEquals(tracker.getLayerId(), Tracker.TrackerType.REAL.toString());

        List<Device> devices = deviceCRUDDao.getAll();
        Assert.assertEquals(1,devices.size());

        Device device = devices.get(0);
        Assert.assertEquals(0.0,device.getLastValidHeading());

        Assert.assertEquals(0.0,device.getLastValidLatitude());

        Assert.assertEquals(0.0,device.getLastValidLongitude());

        tracker = trackerService.getTracker(newTrackerId);
        Assert.assertEquals(true, tracker.isActive());
    }

    @Test
    public void shouldLoadNewDrone() {
        Device device = new Device();
        device.setAccountID("demo");
        device.setDeviceID("1");
        device.setDescription("this is a description");
        device.setLastValidHeading(22.0);
        device.setLastValidLatitude(55.0);
        device.setLastValidLongitude(66.0);

        deviceCRUDDao.create(device);

        trackerService.initTrackerService();

        Tracker tracker = trackerService.getTracker("1");
        Assert.assertEquals("1",tracker.getId());
        Assert.assertEquals("this is a description",tracker.getName());

        MapPoint mapPoint = tracker.getCurrentPosition();
        Assert.assertEquals(55.0,mapPoint.getLatitude());
        Assert.assertEquals(66.0,mapPoint.getLongitude());

    }

    @Test
    public void shouldRemoveDrone() {
        String newTrackerId = "11223344";
        trackerService.registerTracker(newTrackerId, "this is a new name");
        Tracker tracker = trackerService.getTracker(newTrackerId);
        Assert.assertEquals(tracker.getLayerId(), Tracker.TrackerType.REAL.toString());
        trackerService.remove(newTrackerId);
        tracker = trackerService.getTracker(newTrackerId);
        Assert.assertNull(tracker);
    }

    @Test
    public void shouldUpdateNewDrone() {
        String newTrackerId = "11223344";
        String oldName = "this is an old name";
        trackerService.registerTracker(newTrackerId, oldName);
        Tracker tracker = trackerService.getTracker(newTrackerId);
        Assert.assertEquals(tracker.getLayerId(), Tracker.TrackerType.REAL.toString());

        String newName = "this is the new name";
        tracker.setName(newName);
        String simPhone = "11223344sim";
        tracker.setSimPhone(simPhone);
        String imeiNumber = "11223344imei";
        tracker.setImeiNumber(imeiNumber);
        MapPoint newMappoint = new MapPoint(1144,4444);
        tracker.setCurrentPosition(newMappoint);

        trackerService.update(tracker);
        tracker = trackerService.getTracker(newTrackerId,true);
        Assert.assertEquals(newName,tracker.getName());
        Assert.assertEquals(simPhone,tracker.getSimPhone());
        Assert.assertEquals(imeiNumber,tracker.getImeiNumber());

    }

    // should have an independent EventData service, but now using CrudDaoImpl for creating new entry
    @Test
    public void shouldCreateNewEventData() {
        String newTrackerId = "11223344";
        trackerService.registerTracker(newTrackerId,"");

        EventData eventData = new EventData();
        eventData.setDeviceID(newTrackerId);
        eventData.setAccountID("demo");
        eventData.setLongitude(1121);
        eventData.setLatitude(1131);
        eventData.setAltitude(1141);
        eventData.setStatusCode(1151);
        eventDataCRUDDao.create(eventData);

        Properties properties = new Properties();
        properties.put("accountID",eventData.getAccountID());
        properties.put("deviceID",eventData.getDeviceID());
        properties.put("statusCode",eventData.getStatusCode());
        properties.put("timestamp",eventData.getTimestamp());

        List<EventData> eventDataList = eventDataCRUDDao.getByProperties(properties);
        Assert.assertNotSame(0,eventDataList.size());

        for(EventData eventData1: eventDataList) {
            eventDataCRUDDao.delete(eventData1);
        }

        eventDataList = eventDataCRUDDao.getByProperties(properties);
        Assert.assertEquals(0,eventDataList.size());
    }
}
