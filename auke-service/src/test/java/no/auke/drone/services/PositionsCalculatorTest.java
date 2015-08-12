package no.auke.drone.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import no.auke.drone.domain.SimpleTracker;
import no.auke.drone.domain.Tracker.TrackerType;
import no.auke.drone.domain.TrackerData;
import no.auke.drone.services.PositionCalculatorService;
import no.auke.drone.services.impl.PositionCalculatorServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
@PrepareForTest({PositionCalculatorService.class})
@Ignore
public class PositionsCalculatorTest {
	
    private static ExecutorService executor = Executors.newCachedThreadPool();
    public static ExecutorService getExecutor() {
        return executor;
    }
	
	PositionCalculatorService calculator;
	SimpleTracker tracker1;
	SimpleTracker tracker2;
	SimpleTracker tracker3;

	@Before
	public void setUp() throws Exception {
		
		tracker1 = mock(SimpleTracker.class);
		tracker2 = mock(SimpleTracker.class);
		tracker3 = mock(SimpleTracker.class);
		
		when(tracker1.getId()).thenReturn("TRACKER1");
		when(tracker2.getId()).thenReturn("TRACKER2");
		when(tracker3.getId()).thenReturn("TRACKER3");

		when(tracker1.getLayerId()).thenReturn("SIMULATED");
		when(tracker2.getLayerId()).thenReturn("REAL");
		when(tracker3.getLayerId()).thenReturn("REAL");
		
		when(tracker1.getTrackerType()).thenReturn(TrackerType.SIMULATED);
		when(tracker2.getTrackerType()).thenReturn(TrackerType.REAL);
		when(tracker3.getTrackerType()).thenReturn(TrackerType.REAL);
		
		TrackerData.getInstance().register(tracker1);
		TrackerData.getInstance().register(tracker2);
		TrackerData.getInstance().register(tracker3);
		
		assertEquals(3,TrackerData.getInstance().getTrackers().size());
		assertEquals(2,TrackerData.getInstance().getLayers().size());
		
		PositionCalculatorServiceImpl.CALC_FREQUENCY=10;

//		calculator=new PositionCalculatorImpl(getExecutor(),true);
			
	}

	@After
	public void tearDown() throws Exception {
		
		getExecutor().shutdownNow();

	}

	
	@Test
	public void test_loop_run() {
		
		try {
		
			Thread.sleep(5000);

		} catch (InterruptedException e) {
		}
		
		verify(tracker1,atLeast(1)).calculate();	
		verify(tracker2,never()).calculate();	
		verify(tracker3,never()).calculate();	
	
	}

}
