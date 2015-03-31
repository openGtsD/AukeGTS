package no.auke.drone.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.PositionUnit;

/**
 * @author thaihuynh Using for test
 */
public class DummyCreator {

    List<PositionUnit> list = new ArrayList<PositionUnit>();
    List<MapPoint> points = new ArrayList<MapPoint>();

    public DummyCreator() {
        list.clear();
        points.add(new MapPoint(10.823099, 106.629664));// HCM
        points.add(new MapPoint(59.913869, 10.752245));// OSLO
        points.add(new MapPoint(55.378051, -3.435973));// UK
        points.add(new MapPoint(51.507351, -0.127758));// London
        makeRandomDrones();
    }

    public MapPoint generateRandomMapPoint(MapPoint l) {
        Random random = new Random();

        // Convert radius from meters to degrees
//        double radiusInDegrees = radius / 111000f;

        double v = random.nextDouble();
        double t = 2 * Math.PI * v;
        double x = Math.cos(t);
        double y = Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(l.getLatitude());

        double foundLongitude = new_x + l.getLongitude();
        double foundLatitude = y + l.getLatitude();
        MapPoint rl = new MapPoint(foundLatitude, foundLongitude);
        return rl;
    }

    public List<PositionUnit> getAllTrack() {
        return list;
    }

    public static PositionUnit createPositionUnit(MapPoint location) {
        return createPositionUnit(UUID.randomUUID().toString(), 1413835537, location.getLatitude(),
                location.getLongitude(), 5, 0.0);
    }

    private static PositionUnit createPositionUnit(String id, long time, double lat, double lon, double altitude,
            double speed) {
        PositionUnit unit = new PositionUnit();
        unit.setId(id);
        unit.setTime(time);
        unit.setLat(lat);
        unit.setLon(lon);
        unit.setAltitude(altitude);
        unit.setSpeed(speed);
        return unit;
    }

    public List<PositionUnit> getTrackByGeo(double lat, double lon) {
        List<PositionUnit> result = new ArrayList<PositionUnit>();
        for (PositionUnit positionUnit : list) {
            if (positionUnit.hasGeo(lat, lon)) {
                result.add(positionUnit);
            }
        }
        return result;
    }

    public PositionUnit getDrone(String droneId) {
        for (PositionUnit positionUnit : list) {
            if (positionUnit.hasID(droneId)) {
                return positionUnit;
            }
        }
        return null;
    }

    public List<PositionUnit> getTrackWithinView(double upperLat, double upperLon, double lowerLat, double lowerLon) {
        List<PositionUnit> result = new ArrayList<PositionUnit>();
        for (PositionUnit positionUnit : list) {
            if (positionUnit.withinView(upperLat, upperLon, lowerLat, lowerLon)) {
                result.add(positionUnit);
            }
        }
        return result;
    }

    public List<PositionUnit> makeRandomDrones() {
        for (int i = 0; i < points.size(); i++) {
            MapPoint point = points.get(i);
            for(int j = 1; j <= 1000; j ++) {
                MapPoint rd = generateRandomMapPoint(point);
                list.add(createPositionUnit("Drone" + i + "" + j, System.currentTimeMillis(), rd.getLatitude(), rd.getLongitude(),
                        2 * i, i * 2));
            }
        }
        return list;
    }

    public MapPoint makeLocation(int i, int j) {
        return null;
    }

    public List<PositionUnit> getList() {
        return list;
    }
}
