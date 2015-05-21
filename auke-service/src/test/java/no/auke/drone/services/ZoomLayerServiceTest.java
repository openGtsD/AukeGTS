package no.auke.drone.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.SimpleTracker;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.Tracker.TrackerType;
import no.auke.drone.domain.TrackerData;
import no.auke.drone.services.PositionCalculator;
import no.auke.drone.services.impl.PositionCalculatorImpl;
import no.auke.drone.services.impl.ZoomLayerServiceImpl;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest({ZoomLayerServiceTest.class})

public class ZoomLayerServiceTest {
	
	List<ZoomLayerServiceImpl> services = new ArrayList<ZoomLayerServiceImpl>();
	
	ZoomLayerServiceImpl service_level1;
	ZoomLayerServiceImpl service_level10;
    
	@Before
	public void setUp() throws Exception {
		service_level1 = new ZoomLayerServiceImpl(TrackerData.getInstance().getTrackerLayer("DEFAULT"), 1);
		service_level10 = new ZoomLayerServiceImpl(TrackerData.getInstance().getTrackerLayer("DEFAULT"), 10);
	}

	@After
	public void tearDown() throws Exception {
		TrackerData.clear();
	}

	@Test
	public void test_calulate_average_level1() {
		
		for(double pos=0;pos<10;pos+=0.01) {
			
			SimpleTracker tracker = new SimpleTracker();
			
			tracker.setId("TRACKER"+pos);
			tracker.setLayerId("DEFAULT");
			
			tracker.getCurrentPosition().setLatitude(pos);
			tracker.getCurrentPosition().setLongitude(pos);
			TrackerData.getInstance().register(tracker);
			
		}
		
		assertEquals(1001,TrackerData.getInstance().getTrackers().size());
		service_level1.calculate();
		assertEquals(1,service_level1.getPositions().size());
		
		ArrayList<Tracker> pos = new ArrayList<Tracker>(service_level1.getPositions());
		assertEquals(5,pos.get(0).getCurrentPosition().getLongitude(),0.5);
		assertEquals(5,pos.get(0).getCurrentPosition().getLatitude(),0.5);

        // checking inner trackers
        assertEquals(1,service_level1.getIncludedTrackerIds().size());
        assertEquals(1001,service_level1.getIncludedTrackerIds().get(pos.get(0).getId()).size());
	}	

	
	@Test
	public void test_calulate_average_level10() {
		
		for(double pos=0;pos<0.1;pos+=0.0001) {
			
			SimpleTracker tracker = new SimpleTracker();
			
			tracker.setId("TRACKER"+pos);
			tracker.setLayerId("DEFAULT");
			
			tracker.getCurrentPosition().setLatitude(pos);
			tracker.getCurrentPosition().setLongitude(pos);
			TrackerData.getInstance().register(tracker);
			
		}
		
		assertEquals(1000,TrackerData.getInstance().getTrackers().size());
		service_level10.calculate();
		assertEquals(3,service_level10.getPositions().size());
		
		ArrayList<Tracker> pos = new ArrayList<Tracker>(service_level10.getPositions());

		assertEquals(0.017,pos.get(0).getCurrentPosition().getLongitude(),0.001);
		assertEquals(0.017,pos.get(0).getCurrentPosition().getLatitude(),0.001);

		assertEquals(0.053,pos.get(1).getCurrentPosition().getLongitude(),0.001);
		assertEquals(0.053,pos.get(1).getCurrentPosition().getLatitude(),0.001);
		
		assertEquals(0.085,pos.get(2).getCurrentPosition().getLongitude(),0.001);
		assertEquals(0.085,pos.get(2).getCurrentPosition().getLatitude(),0.001);

        // checking inner trackers
        assertEquals(3,service_level10.getIncludedTrackerIds().size());
        assertEquals(352,service_level10.getIncludedTrackerIds().get(pos.get(0).getId()).size());
        assertEquals(351,service_level10.getIncludedTrackerIds().get(pos.get(1).getId()).size());
        assertEquals(297,service_level10.getIncludedTrackerIds().get(pos.get(2).getId()).size());

	}

}
