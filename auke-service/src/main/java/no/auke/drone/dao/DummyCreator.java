package no.auke.drone.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import no.auke.drone.domain.PositionUnit;

/**
 * @author thaihuynh Using for test
 */
public class DummyCreator {

    public class Location {
        private double lat, lon;

        public Location(double lat, double lon) {
            this.lon = lon;
            this.lat = lat;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }

    List<PositionUnit> list = new ArrayList<PositionUnit>();

    public DummyCreator() {
        Location hcm = makeLocation(10.8230989, 106.6296638);// Using defautl location HCM city for test
        list.add(createPositionUnit("Drone0", 1413835537, hcm.getLat(), hcm.getLon(), 5, 0.0));
        for (int i = 1; i <= 30; i++) {
          Location rd = generateRandomLoc(hcm, 100000);
          list.add(createPositionUnit("Drone" + i, 1413835537, rd.getLat(), rd.getLon(), 2 * i, i * 2));
      }
    }

    public Location makeLocation(double lat, double lon) {
        return new Location(lat, lon);
    }

    public Location generateRandomLoc(Location l, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(l.getLat());

        double foundLongitude = new_x + l.getLon();
        double foundLatitude = y + l.getLat();

        //System.out.println("Longitude: " + foundLongitude + "  Latitude: " + foundLatitude);

        Location rl = new Location(foundLatitude, foundLongitude);
        return rl;
    }

    public List<PositionUnit> getAllTrack() {
        return list;
    }

    public static PositionUnit createPositionUnit(Location location) {
        return createPositionUnit(UUID.randomUUID().toString(), 1413835537, location.getLat(), location.getLon(), 5, 0.0);
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

	public List<PositionUnit> getTrackWithinView(double lat, double lon, double height,
			double width) {
        List<PositionUnit> result = new ArrayList<PositionUnit>();
        for (PositionUnit positionUnit : list) {
            if (positionUnit.withinView(lat, lon, height,width)) {
                result.add(positionUnit);
            }
        }
        return result;
	}
}
