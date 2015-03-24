package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyduong on 3/24/2015.
 */
public class DroneData implements Subject {
    private List<Observer> drones;

    public DroneData() {
        drones = new ArrayList<Observer>();
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
