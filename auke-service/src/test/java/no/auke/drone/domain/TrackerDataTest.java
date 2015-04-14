package no.auke.drone.domain;


import no.auke.drone.domain.Tracker.TrackerType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
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
		
		//when(tracker1.getLayerid()).thenReturn("layer1");
		//when(tracker2.getLayerid()).thenReturn("layer1");
		//when(tracker3.getLayerid()).thenReturn("layer1");

		TrackerData.getInstance().register(tracker1);
		TrackerData.getInstance().register(tracker2);
		TrackerData.getInstance().register(tracker3);
		
		assertEquals(3,TrackerData.getInstance().getTrackers().size());
		assertEquals(2,TrackerData.getInstance().getLayers().size());
		
	}

	@After
	public void tearDown() throws Exception {
		

	}

	
	@Test
	public void test_add_layer() {
		
		SimpleTracker tracker4 = mock(SimpleTracker.class);
		when(tracker4.getId()).thenReturn("tracker4");
		when(tracker4.getLayerid()).thenReturn("layer4");		

		assertEquals(2,TrackerData.getInstance().getLayers().size());		
		
		TrackerData.getInstance().register(tracker4);
		
		assertEquals(3,TrackerData.getInstance().getLayers().size());		
	}

}
