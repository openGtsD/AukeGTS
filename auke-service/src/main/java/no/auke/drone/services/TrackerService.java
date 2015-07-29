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
    void initTrackerService();
    Tracker registerTracker(String id, String name);
    Tracker registerTracker(Tracker tracker);
    Tracker registerTracker(Tracker tracker, boolean persist);
    Collection<Tracker> removeAll(String layerId);
    Collection<Tracker> removeAll();
    Tracker remove(String trackerId);
    Tracker getTracker(String id);
    Tracker getTracker(String id, boolean refresh);
    TrackerLayer getTrackerLayer(String trackerLayer);
    Collection<TrackerLayer> getTrackerLayers();
    Collection<Tracker> getAll(String layerId);
    Collection<Tracker> getAll();
    Collection<Tracker> getTrackersByIds(List<String> ids);
    Collection<Tracker> getLatestRegisteredTrackers(String trackerLayer);
    Collection<Tracker> getLongestFlightTrackers(String trackerLayer);
    Tracker move(String id, Integer speed, Integer course);
    Tracker stop(String id);
    Tracker start(String id);
    Tracker update(Tracker tracker);
    void updateActiveTracker(Tracker tracker);
    Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom, String layerId);
    
    void stopService();
	void calculateAll();
    
	Collection<Tracker> getActiveTrackers();
    Collection<Tracker> getActiveTrackers(String layerId);

    Collection<Tracker> getPassiveTrackers();
    Collection<Tracker> getPassiveTrackers(String layerId);
}
