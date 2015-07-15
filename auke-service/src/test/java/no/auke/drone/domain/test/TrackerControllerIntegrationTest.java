package no.auke.drone.domain.test;

import junit.framework.Assert;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.Device;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerData;
import no.auke.drone.domain.TrackerLayer;
import no.auke.drone.services.TrackerService;
import no.auke.drone.utils.YmlPropertiesPersister;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by huyduong on 4/13/2015.
 */
public class TrackerControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TrackerService trackerService;

    @Autowired
    CRUDDao<Device> deviceCRUDDao;

    @Autowired
    private YmlPropertiesPersister propertiesPersister;

    @Before
    public void init() {
        deviceCRUDDao.setPersistentClass(Device.class);
        deviceCRUDDao.deleteAll();
        trackerService.removeAll();
        for(TrackerLayer trackerLayer : TrackerData.getInstance().getLayers()) trackerLayer.getActiveTrackers().clear();
        trackerService.registerTracker("id-test-1", "new name");
        trackerService.registerTracker("id-test-2", "new name");
        trackerService.registerTracker("id-test-3","new name");
        trackerService.registerTracker("id-test-4","new name");
        trackerService.registerTracker("id-test-5","new name");
        trackerService.registerTracker("id-test-6","new name");
        trackerService.registerTracker("id-test-7","new name");


    }

    @After
    public void tearDown() {
        trackerService.removeAll();
    }

    @Test
    public void shouldTestAllTrackers() {
        Collection<Tracker> trackers = trackerService.getAll(Tracker.TrackerType.REAL.toString());
        Assert.assertEquals(7, trackers.size());

        Tracker tracker = trackerService.registerTracker("id-test","new name");
        Assert.assertNotNull(tracker.getCurrentPosition());

        trackers = trackerService.getAll(Tracker.TrackerType.REAL.toString());
        Assert.assertEquals(8, trackers.size());

        trackerService.remove("id-test");

        trackers = trackerService.getAll(Tracker.TrackerType.REAL.toString());
        Assert.assertEquals(7, trackers.size());

        trackers = trackerService.getLatestRegisteredTrackers("REAL");
        Assert.assertEquals(7, trackers.size());
        Iterator<Tracker> trackerIterator = trackers.iterator();
        int i = 7;
        while(trackerIterator.hasNext()) {
            tracker = trackerIterator.next();
            Assert.assertEquals("id-test-" + i,tracker.getId());
            i --;
        }

        trackers = trackerService.getLongestFlightTrackers("REAL");
        Assert.assertEquals(7, trackers.size());
    }
}
