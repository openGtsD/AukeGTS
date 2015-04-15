package no.auke.drone.services;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.Tracker.TrackerType;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface TrackerService {
	
    Tracker registerTracker(String id, String name);
    Tracker removeTracker(String id);
    Tracker getTracker(String id);
    Collection<Tracker> getAll(String layerId);
    Collection<Tracker> getAll();
    Tracker move(String id, Integer speed, Integer course);
    Tracker stop(String id);
    Tracker start(String id);
    Tracker update(Tracker tracker);
    Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom, String layerId);
    
    void stopService();
    
    
}
