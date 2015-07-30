package no.auke.drone.services;


import junit.framework.Assert;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.*;
import no.auke.drone.domain.test.AbstractIntegrationTest;

import no.auke.drone.entity.TrackerDB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;


public class TrackerServiceTest extends AbstractIntegrationTest {

    @Autowired
	TrackerService service;

    @Autowired
    CRUDDao<Device> deviceCRUDDao;

    @Autowired
    CRUDDao<TrackerDB> trackerDBCRUDDao;
	
	@Before
	public void setUp() throws Exception {
        deviceCRUDDao.setPersistentClass(Device.class);
        deviceCRUDDao.deleteAll();

        trackerDBCRUDDao.setPersistentClass(TrackerDB.class);
        trackerDBCRUDDao.deleteAll();

        service.removeAll();
	}

	@After
	public void tearDown() throws Exception {
        service.removeAll();
        Collection<Tracker> trackers = service.getAll();
        Assert.assertEquals(0,trackers.size());
    }

	@Test
	public void test_register() {

		service.registerTracker("1", "");
		service.registerTracker("1", "");
		service.registerTracker("2", "");
		service.registerTracker("3", "");

		assertEquals(3,service.getAll(Tracker.TrackerType.REAL.toString()).size());
        assertEquals(3,service.getActiveTrackers(Tracker.TrackerType.REAL.toString()).size());
        assertEquals(0,service.getPassiveTrackers(Tracker.TrackerType.REAL.toString()).size());
		assertEquals(2, service.getTrackerLayers().size());
    }
	
	@Test
	public void test_remove() {

		service.registerTracker("1", "");
        assertEquals(1,service.getAll(Tracker.TrackerType.REAL.toString()).size());

        assertEquals(1,service.getActiveTrackers(Tracker.TrackerType.REAL.toString()).size());
        assertEquals(0,service.getPassiveTrackers(Tracker.TrackerType.REAL.toString()).size());

		service.remove("1");
        assertEquals(0,service.getAll(Tracker.TrackerType.REAL.toString()).size());

        assertEquals(0,service.getActiveTrackers(Tracker.TrackerType.REAL.toString()).size());
        assertEquals(0,service.getPassiveTrackers(Tracker.TrackerType.REAL.toString()).size());

    }
	
	@Test
	public void test_gettracker() {

		service.registerTracker("1", "");
		assertNotNull(service.getTracker("1"));
    }
	
	@Test
	public void test_start_stop() {

		service.registerTracker("1", "");
		assertTrue(service.getTracker("1").isMoving());
		
		service.stop("1");
		assertFalse(service.getTracker("1").isMoving());

		service.start("1");
		assertTrue(service.getTracker("1").isMoving());
    }

	@Test
	public void test_calulateAll_DEFAULT() {

		service.registerTracker("1", "");
		service.registerTracker("2", "");
		
		service.calculateAll();
		
		TrackerLayer layer = TrackerData.getInstance().getTrackerLayer(Tracker.TrackerType.REAL.toString());
		assertNotNull(layer);
		
		for(ZoomLayerService zlayer:layer.getZoomLayers()) {
			assertEquals(1,zlayer.getPositions().size());
		}	
		
		assertEquals(2, layer.getActiveTrackers().size());

    }
	
	@Test
	public void test_loadWithinView_DEFAULT() {
        service.registerTracker("1", "");

        service.registerTracker("2", "");

		assertEquals(1,service.loadWithinView(new BoundingBox(-90,-180,90,180), 1, Tracker.TrackerType.REAL.toString()).size());
		assertEquals(2,service.loadWithinView(new BoundingBox(-90,-180,90,180), 20, Tracker.TrackerType.REAL.toString()).size());

		service.calculateAll();

		assertEquals(1,service.loadWithinView(new BoundingBox(-90,-180,90,180), 1, Tracker.TrackerType.REAL.toString()).size());
		assertEquals(1,service.loadWithinView(new BoundingBox(-90,-180,90,180), 3, Tracker.TrackerType.REAL.toString()).size());
		assertEquals(1,service.loadWithinView(new BoundingBox(-90,-180,90,180), 5, Tracker.TrackerType.REAL.toString()).size());
		assertEquals(1,service.loadWithinView(new BoundingBox(-90,-180,90,180), 10, Tracker.TrackerType.REAL.toString()).size());
		assertEquals(2,service.loadWithinView(new BoundingBox(-90,-180,90,180), 14, Tracker.TrackerType.REAL.toString()).size());
		
		assertEquals(2,service.loadWithinView(new BoundingBox(-90,-180,90,180), 15, Tracker.TrackerType.REAL.toString()).size());
		assertEquals(2, service.loadWithinView(new BoundingBox(-90, -180, 90, 180), 20, Tracker.TrackerType.REAL.toString()).size());
    }
}
