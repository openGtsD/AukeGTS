package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import no.auke.drone.application.impl.SimpleTrackerFactory;
import no.auke.drone.services.impl.LayerServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/24/2015.
 */

// LHA: maybe this object is the layer head ??
//

public class TrackerData {

    private static final Logger logger = LoggerFactory.getLogger(TrackerData.class);

    private static TrackerData trackerData;
    private Map<String, LayerServiceImpl> layerMap;

    public Collection<LayerServiceImpl> getLayers() {
        return layerMap.values();
    }

    public static synchronized TrackerData getInstance() {
        if (trackerData == null) {
            trackerData = new TrackerData();
        }
        return trackerData;
    }

    public static void clear() {
        trackerData = null;
    }

    private TrackerData() {
        layerMap = new ConcurrentHashMap<>();
        layerMap.put("REAL", new LayerServiceImpl("REAL"));
        layerMap.put("SIMULATED", new LayerServiceImpl("SIMULATED", true));
    }

    public LayerServiceImpl getTrackerLayer(String layerId) {

        LayerServiceImpl layer = layerMap.get(StringUtils.upperCase(layerId));
        if (layer == null) {
            // add a new layer
            layer = new LayerServiceImpl(layerId);
            layerMap.put(StringUtils.upperCase(layer.getLayerName()), layer);
        }
        return layer;

    }

    public Collection<Tracker> getTrackers() {
        return getTrackers(null);
    }

    public Collection<Tracker> getActiveTrackers() {
        return getActiveTrackers(null);
    }

    public Collection<Tracker> getActiveTrackers(String layerId) {
        return getTrackerLayer(layerId).getActiveTrackers();
    }

    public Collection<Tracker> getPassiveTrackers() {
        return getPassiveTrackers(null);
    }

    public Collection<Tracker> getPassiveTrackers(String layerId) {
        return getTrackerLayer(layerId).getPassiveTrackers();
    }

    public Collection<Tracker> getTrackers(String layerId) {
        List<Tracker> result = new ArrayList<>();

        if (StringUtils.isEmpty(layerId)) {
            for (LayerServiceImpl layer : layerMap.values()) {
                result.addAll(layer.getTrackers());
            }
        } else {
            result.addAll(getTrackerLayer(layerId).getTrackers());
        }

        return result;
    }

    private void updateLayerReference(Tracker oldTracker, Tracker newTracker) {
        if (!StringUtils.trimToEmpty(oldTracker.getLayerId()).equals(StringUtils.trimToEmpty(newTracker.getLayerId()))) {
            getTrackerLayer(oldTracker.getLayerId()).removeTracker(oldTracker);
            getTrackerLayer(newTracker.getLayerId()).addTracker(oldTracker);
            oldTracker.setLayerId(newTracker.getLayerId());
        }
    }

    private void update(Tracker oldTracker, Tracker newTracker) {

        oldTracker.setName(newTracker.getName());
        oldTracker.setDescription(newTracker.getDescription());
        oldTracker.setContactInfo(newTracker.getContactInfo());
        oldTracker.setStoredTrips(newTracker.isStoredTrips());
        oldTracker.setTrackerPrefix(newTracker.getTrackerPrefix());
        oldTracker.setImeiNumber(newTracker.getImeiNumber());

        oldTracker.setTrackerType(newTracker.getTrackerType());
        oldTracker.setOwner(newTracker.getOwner());
        oldTracker.setSimPhone(newTracker.getSimPhone());
        oldTracker.setModifiedDate(new Date());

        updateLayerReference(oldTracker, newTracker);
    }

    public Tracker update(Tracker newTracker) {
        Tracker tracker = getTracker(newTracker.getId());
        if (tracker == null) {
            tracker = new SimpleTrackerFactory().create(newTracker);
            TrackerData.getInstance().register(tracker);
        }

        if (tracker.equals(newTracker)) {
            return tracker;// do nothing
        }

        update(tracker, newTracker);
        return tracker;
    }

    public Tracker getTracker(String trackerId) {

        for (LayerServiceImpl trackerLayer : layerMap.values()) {
            if (trackerLayer.exists(trackerId)) {
                return trackerLayer.getTracker(trackerId);
            }
        }
        return null;

    }

    public Tracker register(Tracker tracker) {
        if (tracker.getLayerId() != null) {
            getTrackerLayer(tracker.getLayerId()).addTracker(tracker);
        }
        return  tracker;
    }

    public void remove(Tracker tracker) {
        if (tracker.getLayerId() != null) {
            getTrackerLayer(tracker.getLayerId()).removeTracker(tracker);
        }
    }
  
    public boolean exists(String layerId) {
        return layerMap.containsKey(layerId);
    }

    public void startCalculate() {
        for (LayerServiceImpl layer : layerMap.values()) {
            layer.startCalculate();
        }

    }

}
