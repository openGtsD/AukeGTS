package no.auke.drone.domain.test;

import no.auke.drone.dao.impl.SimpleDroneFactory;
import no.auke.drone.domain.Drone;
import no.auke.drone.domain.DroneData;
import no.auke.drone.domain.Observer;
import no.auke.drone.domain.PositionUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by huyduong on 3/29/2015.
 */

public class DroneDataTest {
    private DroneData droneData;

    @Before
    public void setUp() {
        droneData = DroneData.getInstance(false);
    }

    @After
    public void tearDown() {
        // remove all drones available
        for(Drone drone: droneData.getDrones().values()) {
            droneData.remove((Observer)drone);
        }

        // stop any threads running
        droneData.getPositionCalculator().stopCalculate();    }

    @Test
    public void shouldInitDroneData() {
        Assert.assertEquals(0,droneData.getDrones().size());
    }

    @Test
    public void shouldRegisterNewDrone() {
        Drone drone = new SimpleDroneFactory().createDrone("drone1","my drone");
        droneData.register((Observer)drone);
        Assert.assertEquals(1,droneData.getDrones().size());

        Drone newDrone = new SimpleDroneFactory().createDrone("drone2","my drone2");
        droneData.register((Observer)newDrone);
        Assert.assertEquals(2,droneData.getDrones().size());
    }

    @Test
    public void shouldRemoveDrone() {
        Drone drone = new SimpleDroneFactory().createDrone("drone1","my drone");
        droneData.register((Observer)drone);
        Assert.assertEquals(1,droneData.getDrones().size());

        droneData.remove((Observer)drone);
        Assert.assertEquals(0,droneData.getDrones().size());
    }

    @Test
    public void shouldUpdateDroneLocation() {
        Drone drone = new SimpleDroneFactory().createDrone("drone1","my drone");
        droneData.register((Observer)drone);
        Assert.assertEquals(1,droneData.getDrones().size());
        PositionUnit oldPosition = drone.getCurrentPosition();
        drone.calculate();
        PositionUnit newPosition = drone.getCurrentPosition();
        System.out.println("........." + oldPosition.getLat());
        System.out.println("........." + newPosition.getLat());
        Assert.assertNotSame(newPosition,oldPosition);
        Assert.assertSame(oldPosition,drone.getPositions().get(0));
    }
}
