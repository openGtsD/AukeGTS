package no.auke.drone.domain;

import no.auke.drone.dao.impl.SimpleTrackerFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by huyduong on 3/29/2015.
 */

public class TrackerData2Test {
    
	private TrackerData trackerData;

    @Before
    public void setUp() {
        trackerData = TrackerData.getInstance();
    }

    @After
    public void tearDown() {
        // remove all drones available
        for(Tracker tracker : trackerData.getTrackers()) {
            trackerData.remove((Observer) tracker);
        }
   }

    @Test
    public void shouldInitDroneData() {
        Assert.assertEquals(0, trackerData.getTrackers().size());
    }

    @Test
    public void shouldRegisterNewDrone() {
        
    	Tracker tracker = new SimpleTrackerFactory().create("drone1", "my tracker");
        trackerData.register((Observer) tracker);
        Assert.assertEquals(1, trackerData.getTrackers().size());

        Tracker newTracker = new SimpleTrackerFactory().create("drone2", "my drone2");
        trackerData.register((Observer) newTracker);
        Assert.assertEquals(2, trackerData.getTrackers().size());
    }

    @Test
    public void shouldRemoveDrone() {
        Tracker tracker = new SimpleTrackerFactory().create("drone1", "my tracker");
        trackerData.register((Observer) tracker);
        Assert.assertEquals(1, trackerData.getTrackers().size());

        trackerData.remove((Observer) tracker);
        Assert.assertEquals(0, trackerData.getTrackers().size());
    }

    @Test
    public void shouldUpdateDroneLocation() {
        Tracker tracker = new SimpleTrackerFactory().create("drone1", "my tracker");
        trackerData.register((Observer) tracker);
        Assert.assertEquals(1, trackerData.getTrackers().size());
        MapPoint oldPosition = tracker.getCurrentPosition();
        tracker.calculate();
        MapPoint newPosition = tracker.getCurrentPosition();
        System.out.println("........." + oldPosition.getLongitude());
        System.out.println("........." + newPosition.getLatitude());
        Assert.assertNotSame(newPosition,oldPosition);
        Assert.assertSame(oldPosition, tracker.getPositions().get(0));
    }
}
