package no.auke.drone.domain.test;

import junit.framework.Assert;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.Device;
import no.auke.drone.domain.EventData;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerLayer;
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
        /*deviceCRUDDao.deleteAll();
        eventDataCRUDDao.deleteAll();*/
    }

    private void deleteAfterUse(String id) {
        trackerService.removeTracker(id);
        Tracker tracker = trackerService.getTracker(id);
        Assert.assertNull(tracker);
    }

    @Test
    public void shouldRegisterNewDrone() {
        String newTrackerId = "11223344";
        trackerService.registerTracker(newTrackerId, "this is a new name");
        Tracker tracker = trackerService.getTracker(newTrackerId);
        Assert.assertEquals(tracker.getLayerId(), Tracker.TrackerType.REAL.toString());
        deleteAfterUse(newTrackerId);
    }

    @Test
    public void shouldRemoveDrone() {
        String newTrackerId = "11223344";
        trackerService.registerTracker(newTrackerId, "this is a new name");
        Tracker tracker = trackerService.getTracker(newTrackerId);
        Assert.assertEquals(tracker.getLayerId(), Tracker.TrackerType.REAL.toString());
        trackerService.removeTracker(newTrackerId);
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
        trackerService.update(tracker);
        tracker = trackerService.getTracker(newTrackerId);
        Assert.assertEquals(newName,tracker.getName());

        deleteAfterUse(newTrackerId);
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
