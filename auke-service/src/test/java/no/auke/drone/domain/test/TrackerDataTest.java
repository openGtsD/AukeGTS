package no.auke.drone.domain.test;

import no.auke.drone.dao.impl.SimpleTrackerFactory;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerData;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Observer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by huyduong on 3/29/2015.
 */

public class TrackerDataTest {
    private TrackerData trackerData;

    @Before
    public void setUp() {
        trackerData = TrackerData.getInstance(false);
    }

    @After
    public void tearDown() {
        // remove all drones available
        for(Tracker tracker : trackerData.getDrones().values()) {
            trackerData.remove((Observer) tracker);
        }

        // stop any threads running
        trackerData.getPositionCalculator().stopCalculate();    }

    @Test
    public void shouldInitDroneData() {
        Assert.assertEquals(0, trackerData.getDrones().size());
    }

    @Test
    public void shouldRegisterNewDrone() {
        Tracker tracker = new SimpleTrackerFactory().createDrone("drone1","my tracker");
        trackerData.register((Observer) tracker);
        Assert.assertEquals(1, trackerData.getDrones().size());

        Tracker newTracker = new SimpleTrackerFactory().createDrone("drone2","my drone2");
        trackerData.register((Observer) newTracker);
        Assert.assertEquals(2, trackerData.getDrones().size());
    }

    @Test
    public void shouldRemoveDrone() {
        Tracker tracker = new SimpleTrackerFactory().createDrone("drone1","my tracker");
        trackerData.register((Observer) tracker);
        Assert.assertEquals(1, trackerData.getDrones().size());

        trackerData.remove((Observer) tracker);
        Assert.assertEquals(0, trackerData.getDrones().size());
    }

    @Test
    public void shouldUpdateDroneLocation() {
        Tracker tracker = new SimpleTrackerFactory().createDrone("drone1","my tracker");
        trackerData.register((Observer) tracker);
        Assert.assertEquals(1, trackerData.getDrones().size());
        MapPoint oldPosition = tracker.getCurrentPosition();
        tracker.calculate();
        MapPoint newPosition = tracker.getCurrentPosition();
        System.out.println("........." + oldPosition.getLongitude());
        System.out.println("........." + newPosition.getLatitude());
        Assert.assertNotSame(newPosition,oldPosition);
        Assert.assertSame(oldPosition, tracker.getPositions().get(0));
    }
}
