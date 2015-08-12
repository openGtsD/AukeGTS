package no.auke.drone.services;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import junit.framework.Assert;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.AbstractIntegrationTest;
import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.SimpleTracker;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerData;
import no.auke.drone.entity.Device;
import no.auke.drone.entity.TrackerDB;
import no.auke.drone.services.impl.LayerServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class TrackerServiceTest extends AbstractIntegrationTest {

    @Autowired
	TrackerService service;

    @Autowired
    CRUDDao<Device> deviceCRUDDao;

    @Autowired
    CRUDDao<TrackerDB> trackerDBCRUDDao;
    
    private Tracker simpleTracker, simpleTracker2;
	
	@Before
	public void setUp() throws Exception {
	    simpleTracker =  new SimpleTracker("1", "");
	    simpleTracker2 =  new SimpleTracker("2", "");
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

		service.registerTracker(simpleTracker);
		service.registerTracker(simpleTracker2);

		assertEquals(3,service.getAll(Tracker.TrackerType.REAL.toString()).size());
        assertEquals(3,service.getActiveTrackers(Tracker.TrackerType.REAL.toString()).size());
        assertEquals(0,service.getPassiveTrackers(Tracker.TrackerType.REAL.toString()).size());
		assertEquals(2, service.getTrackerLayers().size());
    }
	
	@Test
	public void test_remove() {

		service.registerTracker(simpleTracker);
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

		service.registerTracker(simpleTracker);
		assertNotNull(service.getTracker("1"));
    }
	
	@Test
	public void test_start_stop() {

		service.registerTracker(simpleTracker);
		assertTrue(service.getTracker("1").isMoving());
		
		service.stop("1");
		assertFalse(service.getTracker("1").isMoving());

		service.start("1");
		assertTrue(service.getTracker("1").isMoving());
    }

	@Test
	public void test_calulateAll_DEFAULT() {

		service.registerTracker(simpleTracker);
		service.registerTracker(simpleTracker2);
		
		service.calculateAll();
		
		LayerServiceImpl layer = TrackerData.getInstance().getTrackerLayer(Tracker.TrackerType.REAL.toString());
		assertNotNull(layer);
		
		for(ZoomLayerService zlayer:layer.getZoomLayers()) {
			assertEquals(1,zlayer.getPositions().size());
		}	
		
		assertEquals(2, layer.getActiveTrackers().size());

    }
	
	@Test
	public void test_loadWithinView_DEFAULT() {
        service.registerTracker(simpleTracker);

        service.registerTracker(simpleTracker2);

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
