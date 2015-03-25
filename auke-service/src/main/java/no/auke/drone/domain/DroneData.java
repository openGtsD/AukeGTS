package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyduong on 3/24/2015.
 */
public class DroneData implements Subject {
    private List<Observer> drones;
    private static DroneData droneData;

    public static DroneData getInstance() {
        if(droneData == null) {
            droneData = new DroneData();
        }
        return droneData;
    }

    private DroneData() {
        drones = new ArrayList<Observer>();
    }

    public List<Observer> getDrones() {
        return drones;
    }

    @Override
    public void register(Observer drone) {
        drones.add(drone);
    }

    @Override
    public void remove(Observer drone) {
        drones.remove(drone);
    }

    @Override
    public void notifyAllItems() {
        for(Observer drone : drones) {
            drone.update();
        }
    }
}
