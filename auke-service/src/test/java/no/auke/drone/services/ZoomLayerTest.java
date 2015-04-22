package no.auke.drone.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
@PrepareForTest({ZoomLayerTest.class})

public class ZoomLayerTest {
	
//    private static ExecutorService executor = Executors.newCachedThreadPool();
//    public static ExecutorService getExecutor() {
//        return executor;
//    }
	
	List<ZoomLayerServiceImpl> services = new ArrayList<ZoomLayerServiceImpl>();
    
	@Before
	public void setUp() throws Exception {

		for(int i=1;i<16;i++) {
			services.add(new ZoomLayerServiceImpl(TrackerData.getInstance().getTrackerLayer("DEFAULT"), i));
		}
	
	}

	@After
	public void tearDown() throws Exception {
		
		TrackerData.clear();
		
		//getExecutor().shutdownNow();

	}

	private void init_trackers(int num) {
		
		Random rnd = new Random(System.nanoTime());
		
		for(int i=0;i<num;i++) {
			
			SimpleTracker tracker = mock(SimpleTracker.class);
			
			when(tracker.getId()).thenReturn("TRACKER" + String.valueOf(i));
			when(tracker.getLayerId()).thenReturn("DEFAULT");
			
			MapPoint point = new MapPoint();
			
			point.setLatitude((rnd.nextDouble() * 180) - 90);
			point.setLongitude((rnd.nextDouble() * 360) - 180);
			
			when(tracker.getCurrentPosition()).thenReturn(point);
			
			TrackerData.getInstance().register(tracker);
			
		}
		
	}

	@Test
	public void test_calculate_100() {
		
		System.out.println("---------");
		Long timecalc = System.currentTimeMillis();
		
		init_trackers(100);
		System.out.println("test_calculate_100: time init " + (System.currentTimeMillis() - timecalc));
		
		timecalc = System.currentTimeMillis();
		
		for(ZoomLayerServiceImpl serv:services) {
			serv.calculate();
			System.out.println("test_calculate_100: zoom " + serv.getZoomFactor() + " num pos " + serv.getPositions().size());
			assertTrue(serv.getPositions().size()>0);
		}
		
		System.out.println("---------");
		System.out.println("test_calculate_100: time calculate " + (System.currentTimeMillis() - timecalc));
	
	}
	
	@Test
	public void test_calculate_test_num_trackers() {
		
		System.out.println("test_calculate_test_num_trackers ---------");
		init_trackers(1000);
		
		for(ZoomLayerServiceImpl serv:services) {

			serv.calculate();
			
			int num = 0;
			for(Tracker tracker:serv.getPositions()) {
				num +=tracker.getNumtrackers();
			}
			
			System.out.println("test_calculate_100: zoom " + serv.getZoomFactor() + " num pos " + serv.getPositions().size() + " num tracker " + num);
			assertEquals(1000,num);
			
		}
		
		System.out.println("---------");
	
	}	
	
	@Test
	public void test_calculate_10000() {
		
		System.out.println("---------");
		Long timecalc = System.currentTimeMillis();
		
		init_trackers(10000);
		System.out.println("test_calculate_10000: time init " + (System.currentTimeMillis() - timecalc));
		
		timecalc = System.currentTimeMillis();
		
		for(ZoomLayerServiceImpl serv:services) {
			serv.calculate();
			System.out.println("test_calculate_10000: zoom " + serv.getZoomFactor() + " num pos " + serv.getPositions().size());
			assertTrue(serv.getPositions().size()>0);
		}
		
		System.out.println("---------");
		System.out.println("test_calculate_10000: time calculate " + (System.currentTimeMillis() - timecalc));
	
	}
	
	@Test
	public void test_calculate_loadWithinView() {
		
		System.out.println("---------");
		Long timecalc = System.currentTimeMillis();
		
		for(double lon=-180;lon<=180;lon+=10) {
			
			for(double lat=-90;lat<=90;lat+=10) {

				SimpleTracker tracker = mock(SimpleTracker.class);
				
				when(tracker.getId()).thenReturn("TRACKER" + String.valueOf(lat) + String.valueOf(lon));
				when(tracker.getLayerId()).thenReturn("DEFAULT");
				
				MapPoint point = new MapPoint();
				
				point.setLatitude(lat);
				point.setLongitude(lon);
				
				when(tracker.getCurrentPosition()).thenReturn(point);
				
				TrackerData.getInstance().register(tracker);

			}
			
		}
		
		System.out.println("test_calculate_loadWithinView: time init " + (System.currentTimeMillis() - timecalc));
		
		timecalc = System.currentTimeMillis();
		
		for(ZoomLayerServiceImpl serv:services) {
			serv.calculate();
			assertTrue(serv.getPositions().size()>0);
		}
		
		System.out.println("---------");
		System.out.println("test_calculate_loadWithinView: time calculate " + (System.currentTimeMillis() - timecalc));		
		
		
		
	}
	
	
//	@Test
//	public void test_calculate_100000() {
//		
//		System.out.println("---------");
//		Long timecalc = System.currentTimeMillis();
//		
//		init_trackers(100000);
//		System.out.println("test_calculate_100000: time init " + (System.currentTimeMillis() - timecalc));
//		
//		timecalc = System.currentTimeMillis();
//		
//		for(ZoomLayerServiceImpl serv:services) {
//			serv.calculate();
//			System.out.println("test_calculate_100000: zoom " + serv.getZoomFactor() + " num pos " + serv.getPositions().size());
//			assertTrue(serv.getPositions().size()>0);
//		}
//		
//		System.out.println("---------");
//		System.out.println("test_calculate_100000: time calculate " + (System.currentTimeMillis() - timecalc));
//	
//	}	
	

}
