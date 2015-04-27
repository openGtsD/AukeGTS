package no.auke.drone.domain;


import no.auke.drone.application.impl.SimpleTrackerFactory;
import no.auke.drone.domain.Tracker.TrackerType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TrackerData.class})

public class TrackerDataTest {
	
	SimpleTracker tracker1;
	SimpleTracker tracker2;
	SimpleTracker tracker3;

	@Before
	public void setUp() throws Exception {
		
		tracker1 = mock(SimpleTracker.class);
		tracker2 = mock(SimpleTracker.class);
		tracker3 = mock(SimpleTracker.class);
		
		when(tracker1.getId()).thenReturn("tracker1");
		when(tracker2.getId()).thenReturn("tracker2");
		when(tracker3.getId()).thenReturn("tracker3");

		when(tracker1.getTrackerType()).thenReturn(TrackerType.SIMULATED);
		when(tracker2.getTrackerType()).thenReturn(TrackerType.REAL);
		when(tracker3.getTrackerType()).thenReturn(TrackerType.REAL);
		
		when(tracker1.getLayerId()).thenReturn("layer1");
		when(tracker2.getLayerId()).thenReturn("layer2");
		when(tracker3.getLayerId()).thenReturn("layer3");

		TrackerData.getInstance().register(tracker1);
		TrackerData.getInstance().register(tracker2);
		TrackerData.getInstance().register(tracker3);
		
		assertEquals(3,TrackerData.getInstance().getTrackers().size());
		assertEquals(5,TrackerData.getInstance().getLayers().size());
		
		assertTrue(TrackerData.getInstance().exists("REAL"));
		assertTrue(TrackerData.getInstance().exists("SIMULATED"));
		
		assertTrue(TrackerData.getInstance().exists("LAYER1"));
		assertTrue(TrackerData.getInstance().getTrackerLayer("LAYER1").exists(tracker1.getId()));
		
		
	}

	@After
	public void tearDown() throws Exception {
		TrackerData.clear();
	}
	
	@Test
	public void test_add_layer() {
		
		SimpleTracker tracker4 = mock(SimpleTracker.class);
		when(tracker4.getId()).thenReturn("tracker4");
		when(tracker4.getLayerId()).thenReturn("LAYER4");		
		TrackerData.getInstance().register(tracker4);
		
		assertEquals(6,TrackerData.getInstance().getLayers().size());		
	
	}
	
	
	// TODO: must make better
	@Test
	public void test_withinView() {
		
		TrackerData.clear();
		TrackerLayer layer = TrackerData.getInstance().getTrackerLayer("DEFAULT");
		layer.setRunningAutomatically(false);
		
		TrackerData.getInstance().register((Observer) new SimpleTrackerFactory().create("drone1", "my tracker"));
		
		assertNotNull(layer);
		assertEquals(1,layer.getTrackers().size());
		
		assertEquals(1,layer.loadWithinView(new BoundingBox(-1,-1,1,1), 0).size());
		assertEquals(1,layer.loadWithinView(new BoundingBox(-90,-180,90,180), 0).size());
		assertEquals(1,layer.loadWithinView(new BoundingBox(-90,179,90,-179), 0).size());
		
		TrackerData.getInstance().getTrackerLayer("DEFAULT").getTracker("drone1").setCurrentPosition(new MapPoint(10,10,0,0,0));

		assertEquals(0,layer.loadWithinView(new BoundingBox(0,0,0,0), 0).size());
		assertEquals(1,layer.loadWithinView(new BoundingBox(0,0,100,100), 0).size());

	
	}	

}
