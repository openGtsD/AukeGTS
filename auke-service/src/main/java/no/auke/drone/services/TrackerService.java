package no.auke.drone.services;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;

import java.util.Collection;
import java.util.List;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface TrackerService {
    Tracker registerTracker(String id, String name);
    Tracker removeTracker(String id);
    Tracker getTracker(String id);
    Collection<Tracker> getAll();
    Tracker move(String id, Integer speed, Integer course);
    Tracker stop(String id);
    Tracker start(String id);
    List<Tracker> loadWithinView(BoundingBox boundary);
}