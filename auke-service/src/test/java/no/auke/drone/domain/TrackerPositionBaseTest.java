package no.auke.drone.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class TrackerPositionBaseTest {

	AbstractTrackerBase tracker;

	@Before
	public void setUp() throws Exception {
		tracker = new TrackerSumImpl();
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void test_withinView_360() {

		tracker.setCurrentPosition(new MapPoint(0,0));
		for(int i=-180;i<180;i+=10) {

			assertTrue("fail"+String.valueOf(i),tracker.withinView(0, i, 11, i));
			
		}
	
	}
	
	@Test
	public void test_withinView_360_350_degrees_sector() {

		tracker.setCurrentPosition(new MapPoint(0,0));
		for(int i=-180;i<0;i+=5) {
			
			assertTrue("fail"+String.valueOf(i),tracker.withinView(0, i, 11, i + 350>180?i-10:i + 350));
			
		}

		for(int i=10;i<180;i+=5) {
			
			assertTrue("fail"+String.valueOf(i),tracker.withinView(0, i, 11, i + 350>180?i-10:i + 350));
			
		}

		for(int i=1;i<10;i+=1) {
			
			assertFalse("fail"+String.valueOf(i),tracker.withinView(0, i, 11, i + 350>180?i-10:i + 350));
			
		}
		
	}
	
	@Test
	public void test_withinView_fail_10_degrees() {

		tracker.setCurrentPosition(new MapPoint(0,10));
//		for(int i=-180;i<0;i+=1) {
//			assertFalse("fail"+String.valueOf(i),tracker.withinView(0, i, 11, i + 5>180?i-355:i + 5));
//		}
		
		for(int i=11;i<180;i+=1) {
			assertFalse("fail"+String.valueOf(i),tracker.withinView(0, i, 11, i + 5>180?i-355:i + 5));
		}		
	
	}	
	

	
	
}