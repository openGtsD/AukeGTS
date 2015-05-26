package no.auke.drone.services;

import java.util.Collection;
import java.util.List;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerLayer;

/**
 * Created by huyduong on 3/25/2015.
 */
public interface TrackerService {
	
    Tracker registerTracker(String id, String name);
    Tracker removeTracker(String id);
    Tracker getTracker(String id);
    Tracker getTracker(String id, boolean refresh);
    TrackerLayer getTrackerLayer(String trackerLayer);
    Collection<Tracker> getAll(String layerId);
    Collection<Tracker> getAll();
    Collection<Tracker> getTrackersByIds(List<String> ids);
    Collection<Tracker> getLatestRegisteredTrackers(String trackerLayer);
    Collection<Tracker> getLongestFlightTrackers(String trackerLayer);
    Tracker move(String id, Integer speed, Integer course);
    Tracker stop(String id);
    Tracker start(String id);
    Tracker update(Tracker tracker);
    Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom, String layerId);
    
    void stopService();
	void calculateAll();
    
	// THAI - Need make RSS feed
	Collection<Tracker> getActiveTrackers();
    
}
