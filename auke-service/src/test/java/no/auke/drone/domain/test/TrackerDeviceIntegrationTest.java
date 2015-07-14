package no.auke.drone.domain.test;

import junit.framework.Assert;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.Device;
import no.auke.drone.domain.EventData;
import no.auke.drone.domain.SimpleTracker;
import no.auke.drone.domain.Tracker;
import no.auke.drone.services.TrackerService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huyduong on 7/14/2015.
 */
public class TrackerDeviceIntegrationTest extends AbstractIntegrationTest  {
    @Autowired
    TrackerService trackerService;

    @Autowired
    CRUDDao<Device> deviceCRUDDao;

    @Before
    public void init() {
        deviceCRUDDao.setPersistentClass(Device.class);

        deviceCRUDDao.deleteAll();
    }

    @Test
    public void shouldRegisterTracker() {
        Tracker tracker = new SimpleTracker();
        tracker.setId("tracker1");
        tracker.setImeiNumber("12345");
        tracker.setName("this is the name");
        tracker.setTrackerPrefix("trackerPrefix");
        tracker.setSimPhone("1123344");

        trackerService.registerTracker(tracker);

        Device device = deviceCRUDDao.getById(tracker.getId());

        Device anotherDevice = new Device().from(tracker);

        Assert.assertEquals(device.getDeviceID(),anotherDevice.getDeviceID());
        Assert.assertEquals(device.getAccountID(),anotherDevice.getAccountID());

        Assert.assertEquals(device.getDescription(),anotherDevice.getDescription());
        Assert.assertEquals(device.getImeiNumber(),anotherDevice.getImeiNumber());
        Assert.assertEquals(device.getUniqueID(),anotherDevice.getUniqueID());

    }
}
