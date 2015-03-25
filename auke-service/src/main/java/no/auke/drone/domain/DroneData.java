package no.auke.drone.domain;

import java.util.*;

/**
 * Created by huyduong on 3/24/2015.
 */
public class DroneData implements Subject {
    private Map<String,Observer> drones;
    private static DroneData droneData;

    public static DroneData getInstance() {
        if(droneData == null) {
            droneData = new DroneData();
        }
        return droneData;
    }

    private DroneData() {
        drones = new HashMap<String,Observer>();
        Timer timer = new Timer();
        timer.schedule(new CalculateTask(), 0 ,10000);
    }

    public Map<String,Observer> getDrones() {
        return drones;
    }

    public Observer getDrone(String id) {
        return drones.get(id) != null ? drones.get(id) : null;
    }

    @Override
    public void register(Observer drone) {
        drones.put(drone.getId(), drone);
    }

    @Override
    public void remove(Observer drone) {
        drones.remove(drone.getId());
    }

    @Override
    public void notifyAllItems() {
        System.out.println("........notifying " + drones.size() + " items........");
        for(String droneId : drones.keySet()) {
            drones.get(droneId).update();
        }
        System.out.println(".......notifying finished......");
    }

    class CalculateTask extends TimerTask {
        public void run() {
            notifyAllItems();
        }
    }
}
