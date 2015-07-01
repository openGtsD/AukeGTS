package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerLayer;
import no.auke.drone.domain.TrackerSumImpl;
import no.auke.drone.services.ZoomLayerService;

import org.springframework.stereotype.Service;

@Service
public class ZoomLayerServiceImpl implements ZoomLayerService {

    private ReentrantLock block = new ReentrantLock();
    private Collection<Tracker> positions = new ConcurrentLinkedQueue<Tracker>();

    private Map<Long, TrackerSumImpl> trackerSUM = new ConcurrentHashMap<Long, TrackerSumImpl>();
    private Map<String, List> includedTrackerIds = new ConcurrentHashMap<>();

    private TrackerLayer trackerLayer;
    private int zoomFactor;

    public ZoomLayerServiceImpl() {
    }

    @Override
    public int getZoomFactor() {
        return zoomFactor;
    }

    // Zoom Longitude = ROUND( LONGITUDE / ( 360 / ( 2 ^^(ZOOMLEVEL-1) ) ) , 1)
    // * 10
    // Zoom Latitude = ROUND( LATITUDE / ( 180 / ( 2 ^^(ZOOMLEVEL-1) ) ) , 1) *
    // 10

    public Map<String,List> getIncludedTrackerIds() {
        if(includedTrackerIds == null) {
            includedTrackerIds = new ConcurrentHashMap<>();
        }

        return includedTrackerIds;
    }

    @Override
    public double longitudeWith() {

        return 360 / Math.pow(2, zoomFactor - 1);
    }

    @Override
    public double latitudeWith() {

        return 180 / Math.pow(2, zoomFactor - 1);
    }

    @Override
    public double zoomLongitude(Double longitude) {

        return Math.round((longitude / longitudeWith()) * 10000) / 1000;

    }

    @Override
    public double zoomLatitude(Double latitude) {

        return Math.round((latitude / latitudeWith()) * 10000) / 1000;

    }

    @Override
    public double longitude(Double zoomlongitude) {

        return Math.round((longitudeWith() / 10) * zoomlongitude * 100) / 100;

    }

    @Override
    public double latitude(Double zoomlatitude) {

        return Math.round((latitudeWith() / 10) * zoomlatitude * 100) / 100;

    }

    public ZoomLayerServiceImpl(TrackerLayer trackerLayer, int zoomFactor) {

        this.trackerLayer = trackerLayer;
        this.zoomFactor = zoomFactor;

    }

    private void updateData( Map<Long, TrackerSumImpl> newPositions) {
        positions.clear();
        positions.addAll(newPositions.values());

        // for getting tracker info
        trackerSUM.clear();
        trackerSUM.putAll(newPositions);
    }

    @Override
    public void calculate() {

        Map<Long, TrackerSumImpl> newPositions = new HashMap<Long, TrackerSumImpl>();
        includedTrackerIds.clear();

        for (Tracker tracker : trackerLayer.getActiveTrackers()) {

            double lon = zoomLongitude(tracker.getCurrentPosition().getLongitude());
            double lat = zoomLatitude(tracker.getCurrentPosition().getLatitude());

            Long id = getIndex(lon, lat);

            TrackerSumImpl point;
            if (!newPositions.containsKey(id)) {

                point = new TrackerSumImpl();
                point.setId(String.valueOf(id) + "-" + String.valueOf(getZoomFactor()));
                point.setName("Tracker within long=" + String.valueOf(lon) + " lat=" + String.valueOf(lat));
                point.getCurrentPosition().setLatitude(tracker.getCurrentPosition().getLatitude());
                point.getCurrentPosition().setLongitude(tracker.getCurrentPosition().getLongitude());

                newPositions.put(id, point);

            } else {

                point = newPositions.get(id);

            }

            point.incrementTrackers();

            // LHA: use this to get trackers included in the point
            // See function, getIncludedTracker(Id)

            // LHA: We show average positions for all trackers on current
            // summarized positions

            point.getCurrentPosition().setLatitude(
                    ((point.getCurrentPosition().getLatitude() * point.getNumtrackers()) + tracker.getCurrentPosition()
                            .getLatitude()) / (point.getNumtrackers() + 1));

            point.getCurrentPosition().setLongitude(
                    ((point.getCurrentPosition().getLongitude() * point.getNumtrackers()) + tracker
                            .getCurrentPosition().getLongitude()) / (point.getNumtrackers() + 1));

            // LHA: use this to get trackers included in the point
            // See function, getIncluded(Id)

            if(includedTrackerIds.get(point.getId()) == null) {
                includedTrackerIds.put(point.getId(), new ArrayList<String>());
            }

            includedTrackerIds.get(point.getId()).add(tracker.getId());
        }

        try {

            block.lock();
            updateData(newPositions);

        } finally {

            block.unlock();
        }

    }

    private Long getIndex(double lon, double lat) {
        return (long) ((lon * 100000000L) + lat);
    }

    @Override
    public Collection<Tracker> getPositions() {
        return positions;
    }

    @Override
    public Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom) {

        // zoom factor not in use
        Collection<Tracker> result = new ArrayList<Tracker>();

        if (zoom == getZoomFactor()) {

            try {

                block.lock();

                for (Tracker positionUnit : getPositions()) {

                    if (positionUnit.withinView(boundary.getSouthWestLat(), boundary.getSouthWestLon(),
                            boundary.getNorthEastLat(), boundary.getNorthEastLon())

                    ) {

                        result.add(positionUnit);
                    }
                }

            } finally {

                block.unlock();
            }

        }

        return result;

    }

    // LHA: Use this to get the list of included trackers
    @Override
    public Collection<String> getIncludedTrackers(String trackerId) {
        List<String> includedTrackers = getIncludedTrackerIds().get(trackerId);
        return includedTrackers;
    }

    @Override
    public void clear() {

        try {

            block.lock();
            positions.clear();

        } finally {

            block.unlock();
        }

    }

    @Override
    public List<BoundingBox> getMapAreas() {

        double lonsize = (360 / (Math.pow(2, (getZoomFactor() - 1))));
        double latsize = (180 / (Math.pow(2, (getZoomFactor() - 1))));

        List<BoundingBox> boundaries = new ArrayList<BoundingBox>();

        for (double lon = -180; lon < 180; lon += lonsize) {

            for (double lat = -90; lat < 90; lat += latsize) {
                BoundingBox boundary = new BoundingBox(lat, lon, lat + latsize, lon + lonsize);
                boundaries.add(boundary);
            }

        }

        return boundaries;

    }

}
