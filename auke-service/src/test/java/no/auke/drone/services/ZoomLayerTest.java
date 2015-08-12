package no.auke.drone.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import no.auke.drone.domain.*;
import no.auke.drone.domain.Tracker.TrackerType;
import no.auke.drone.services.PositionCalculatorService;
import no.auke.drone.services.impl.PositionCalculatorServiceImpl;
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
	}

	private void init_trackers(int num) {
		
		Random rnd = new Random(System.nanoTime());
		
		for(int i=0;i<num;i++) {
			
			SimpleTracker tracker = new SimpleTracker();
			
			tracker.setId("TRACKER" + i);
			tracker.setLayerId("DEFAULT");
			
			tracker.getCurrentPosition().setLatitude((rnd.nextDouble() * 180) - 90);
			tracker.getCurrentPosition().setLongitude((rnd.nextDouble() * 360) - 180);
			
			
//			SimpleTracker tracker = mock(SimpleTracker.class);
//			
//			when(tracker.getId()).thenReturn("TRACKER" + String.valueOf(i));
//			when(tracker.getLayerId()).thenReturn("DEFAULT");
//			
//			MapPoint point = new MapPoint();
//			
//			point.setLatitude((rnd.nextDouble() * 180) - 90);
//			point.setLongitude((rnd.nextDouble() * 360) - 180);
//			
//			when(tracker.getCurrentPosition()).thenReturn(point);
			
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
                TrackerSumImpl trackerSum = (TrackerSumImpl) tracker;
				num +=trackerSum.getNumtrackers();
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

				SimpleTracker tracker = new SimpleTracker();
				
				tracker.setId("TRACKER" + String.valueOf(lat) + String.valueOf(lon));
				tracker.setLayerId("DEFAULT");
				
				tracker.getCurrentPosition().setLatitude(lat);
				tracker.getCurrentPosition().setLongitude(lon);
				
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
		
		// test positions
		// test overall position
		BoundingBox boundary =  new BoundingBox(-90, -180, 90, 180);
		for(ZoomLayerServiceImpl serv:services) {
			assertTrue("zoom factor " + serv.getZoomFactor(), serv.loadWithinView(boundary, serv.getZoomFactor()).size()>0);
		}
		
		
	}
	

	
	@Test
	public void test_calculate_loadWithinView_one_position() {
		
		System.out.println("---------");
		System.out.println("test_calculate_loadWithinView_one_position");		

		double lon = 89;
		double lat = 50;
				
		
		SimpleTracker tracker = new SimpleTracker();
		tracker.setId(UUID.randomUUID().toString());
		tracker.setLayerId("DEFAULT");
		tracker.getCurrentPosition().setLongitude(lon);
		tracker.getCurrentPosition().setLatitude(lat);
		TrackerData.getInstance().register(tracker);

		System.out.println("base tracker:" + " lat " + lat + " lon " + lon);			
		
		assertEquals(1,TrackerData.getInstance().getTrackers().size());
		
		for(ZoomLayerServiceImpl serv:services) {
			serv.calculate();
			assertEquals("zoom"+serv.getZoomFactor(),1,serv.getPositions().size());
			
			for(Tracker tracker1:serv.getPositions()) {
				
				System.out.println("zoom tracker:" + serv.getZoomFactor() + " lat " + tracker1.getCurrentPosition().getLatitude() 
						+ " lon " + tracker1.getCurrentPosition().getLongitude());			
				
			}
		}

		assertEquals(1,services.get(1).loadWithinView(new BoundingBox(0, 0, 90, 180), 2).size());
		assertEquals(1,services.get(2).loadWithinView(new BoundingBox(40, 80, 90, 180), 3).size());
		assertEquals(1,services.get(3).loadWithinView(new BoundingBox(40, 80, 90, 180), 4).size());
		assertEquals(1,services.get(4).loadWithinView(new BoundingBox(40, 80, 50, 90), 5).size());
		assertEquals(1,services.get(5).loadWithinView(new BoundingBox(40, 80, 50, 90), 6).size());
		assertEquals(1,services.get(6).loadWithinView(new BoundingBox(40, 80, 50, 90), 7).size());
		assertEquals(1,services.get(7).loadWithinView(new BoundingBox(40, 80, 50, 90), 8).size());
		assertEquals(1,services.get(8).loadWithinView(new BoundingBox(40, 80, 50, 90), 9).size());
		assertEquals(1,services.get(9).loadWithinView(new BoundingBox(40, 80, 50, 90), 10).size());
		assertEquals(1,services.get(10).loadWithinView(new BoundingBox(40, 80, 50, 90),11).size());
		assertEquals(1,services.get(11).loadWithinView(new BoundingBox(40, 80, 50, 90),12).size());
		assertEquals(1,services.get(12).loadWithinView(new BoundingBox(40, 80, 50, 90),13).size());
		assertEquals(1,services.get(13).loadWithinView(new BoundingBox(40, 80, 50, 90),14).size());
		assertEquals(1,services.get(14).loadWithinView(new BoundingBox(40, 80, 50, 90),15).size());
		
	
		
	}	
	
	
	@Test
	public void test_zoomlongitude_zoomLatitude() {
		
//		for(double lon=-180;lon<=180;lon+=10) {
//			
//			for(ZoomLayerServiceImpl serv:services) {
//				System.out.println("zoom " + serv.getZoomFactor() + " lon " + lon + " zoom lon " + serv.zoomLongitude(lon) + " lon3 " + serv.Longitude(serv.zoomLongitude(lon)));				
//			}
//			
//		}
//
//		for(double lat=-90;lat<=90;lat+=10) {
//			
//			//System.out.println("lat " + lat + " zoom lat " + services.get(0).zoomLatitude(lat));
//			
//		}		
		
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
