package no.auke.drone.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huyduong on 4/10/2015.
 */
public class TrackerLayer {
    private String layerName;

    private Map<String,Tracker> trackers;

    public TrackerLayer(String layerName) {
        this.layerName = layerName;
        trackers = new ConcurrentHashMap<>();
    }

    public TrackerLayer() {
        layerName = "default";
        trackers = new ConcurrentHashMap<>();
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public Map<String, Tracker> getTrackers() {
        return trackers;
    }

    public void setTrackers(Map<String, Tracker> trackers) {
        this.trackers = trackers;
    }
}
