package no.auke.drone.services;

import java.util.Collection;
import java.util.Map;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;

public interface LayerService {
    
    ZoomLayerService getZoomLayer(Integer zoom);
    
    Collection<ZoomLayerService> getZoomLayers();
    
    void setZoomLayers(Map<Integer, ZoomLayerService> zoomLayers);
    
    boolean isRunningAutomatically();
    
    void setRunningAutomatically(boolean isRunningAutomatically);
    
    String getLayerName();
    
    void setLayerName(String layerName);
    
    Map<String,Tracker> getPassiveTrackersMap();
    
    Collection<Tracker> getTrackers();
    
    Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom);

    boolean exists(String id);
    
    Tracker getTracker(String trackerId);
    
    void startCalculate();
    
    Collection<Tracker> getPositions();
    
    void calculateAll();;
    
    void calulateTrackerFeed();
    
    void calculateZoomLayers();
}
