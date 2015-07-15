package no.auke.drone.domain;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import no.auke.drone.application.TrackerUpdater;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TrackerPositionBaseTest {

	TrackerPositionBase tracker;
	

	@Before
	public void setUp() throws Exception {
		
		tracker = new TrackerPositionBase() {

			@Override
			public void setName(String name) {}
			@Override
			public String getName() {return null;}
			@Override
			public void setTrackerType(TrackerType name) {}
			@Override
			public TrackerType getTrackerType() {return null;}
			@Override
			public void setFlyer(Person person) {}
			@Override
			public Person getFlyer() {return null;}
			@Override
			public void update() {}
			@Override
			public List<MapPoint> getPositions() {return null;}
			@Override
			public void setPositions(List<MapPoint> positions) {}
			@Override
			public void calculate() {}
			@Override
			public Tracker move(Integer speed, Integer course) {return null;}
			@Override
			public String getSimPhone() {return null;}
			@Override
			public void setSimPhone(String simPhone) {}
			@Override
			public Date getCreateDate() {return null;}
			@Override
			public void setCreateDate(Date date) {}
			@Override
			public Date getModifiedDate() {return null;}
			@Override
			public void setModifiedDate(Date data) {}
			@Override
			public int getNumtrackers() {return 0;}
			@Override
			public void incrementTrackers() {}
			@Override
			public CircularFifoBuffer getLatestPositions() {return null;}
			@Override
			public void setLatestPositions(CircularFifoBuffer latestPositions) {}
			@Override
			public void setTrackerUpdater(TrackerUpdater trackerUpdater) {}
            @Override
            final public String getImeiNumber() {
                return null;
            }
            @Override
            final public void setImeiNumber(String imeiNumber) {
            }
            @Override
            public String getTrackerPrefix() {
                return null;
            }
            @Override
            public void setTrackerPrefix(String prefix) {
                // TODO Auto-generated method stub
                
            }
			};

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
		for(int i=-180;i<0;i+=1) {
			
			assertFalse("fail"+String.valueOf(i),tracker.withinView(0, i, 11, i + 5>180?i-355:i + 5));
			
		}
		
		for(int i=11;i<180;i+=1) {
			
			assertFalse("fail"+String.valueOf(i),tracker.withinView(0, i, 11, i + 5>180?i-355:i + 5));
			
		}		
	
	}	
	

	
	
}