package no.auke.drone.services;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;

import java.util.Collection;
import java.util.List;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface TrackerService {
    Tracker registerDrone(String id, String name);
    Tracker removeDrone(String droneId);
    Tracker getDrone(String id);
    Collection<Tracker> getAll();
    Tracker moveDrone(String id);
    Tracker stopDrone(String id);
    Tracker startDrone(String id);
    List<Tracker> loadDroneWithinView(BoundingBox boundary);
}
