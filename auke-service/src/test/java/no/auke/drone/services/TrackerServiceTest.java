package no.auke.drone.services;


import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.*;
import no.auke.drone.domain.test.AbstractIntegrationTest;
import no.auke.drone.services.impl.TrackerServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;


public class TrackerServiceTest extends AbstractIntegrationTest {

    @Autowired
	TrackerService service;

    @Autowired
    CRUDDao<Device> deviceCRUDDao;
	
	@Before
	public void setUp() throws Exception {
        deviceCRUDDao.setPersistentClass(Device.class);
        deviceCRUDDao.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
        service.stopService();
		assertEquals(0, TrackerData.getInstance().getTrackers().size());
	}

	@Test
	public void test_register() {

		service.registerTracker("1", "");
		service.registerTracker("1", "");
		service.registerTracker("2", "");
		service.registerTracker("3", "");

		assertEquals(3,TrackerData.getInstance().getTrackers(Tracker.TrackerType.REAL.toString()).size());
		assertEquals(2,TrackerData.getInstance().getLayers().size());
        service.stopService();
    }
	
	@Test
	public void test_remove() {

		service.registerTracker("1", "");
		assertEquals(1,TrackerData.getInstance().getTrackers(Tracker.TrackerType.REAL.toString()).size());

		service.removeTracker("1");
		assertEquals(0,TrackerData.getInstance().getTrackers(Tracker.TrackerType.REAL.toString()).size());
        service.stopService();
    }
	
	@Test
	public void test_gettracker() {

		service.registerTracker("1", "");
		assertNotNull(service.getTracker("1"));
        service.stopService();
    }
	
	@Test
	public void test_start_stop() {

		service.registerTracker("1", "");
		assertTrue(service.getTracker("1").isMoving());
		
		service.stop("1");
		assertFalse(service.getTracker("1").isMoving());

		service.start("1");
		assertTrue(service.getTracker("1").isMoving());
        service.stopService();
    }

	@Test
	public void test_calulateAll_DEFAULT() {

		service.registerTracker("1", "");
		service.registerTracker("2", "");
		
		service.calculateAll();
		
		TrackerLayer layer = TrackerData.getInstance().getTrackerLayer("DEFAULT");
		assertNotNull(layer);
		
		for(ZoomLayerService zlayer:layer.getZoomLayers()) {
			assertEquals(1,zlayer.getPositions().size());
		}	
		
		assertEquals(2,layer.getTrackers().size());
        service.stopService();

    }
	
	@Test
	public void test_loadWithinView_DEFAULT() {

		
		service.registerTracker("1", "");
		service.registerTracker("2", "");

		assertEquals(0,service.loadWithinView(new BoundingBox(-90,180,90,-180), 1, "DEFAULT").size());
		assertEquals(2,service.loadWithinView(new BoundingBox(-90,180,90,-180), 20, "DEFAULT").size());

		service.calculateAll();

		assertEquals(1,service.loadWithinView(new BoundingBox(-90,180,90,-180), 1, "DEFAULT").size());
		assertEquals(1,service.loadWithinView(new BoundingBox(-90,180,90,-180), 3, "DEFAULT").size());
		assertEquals(1,service.loadWithinView(new BoundingBox(-90,180,90,-180), 5, "DEFAULT").size());
		assertEquals(1,service.loadWithinView(new BoundingBox(-90,180,90,-180), 10, "DEFAULT").size());
		assertEquals(1,service.loadWithinView(new BoundingBox(-90,180,90,-180), 14, "DEFAULT").size());
		
		assertEquals(2,service.loadWithinView(new BoundingBox(-90,180,90,-180), 15, "DEFAULT").size());	
		assertEquals(2,service.loadWithinView(new BoundingBox(-90,180,90,-180), 20, "DEFAULT").size());
        service.stopService();
    }
}
