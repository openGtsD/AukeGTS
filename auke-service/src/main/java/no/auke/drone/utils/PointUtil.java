package no.auke.drone.utils;

import java.util.Random;

import no.auke.drone.domain.MapPoint;

public class PointUtil {
    
    public static MapPoint generateRandomMapPoint(MapPoint l) {
        
    	Random random = new Random();

        // Convert radius from meters to degrees
        // double radiusInDegrees = radius / 111000f;

        double v = random.nextDouble();
        double t = 2 * Math.PI * v;
        double x = Math.cos(t);
        double y = Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(l.getLatitude());

        double foundLongitude = new_x + l.getLongitude();
        double foundLatitude = y + l.getLatitude();
        
        MapPoint rl = new MapPoint(foundLatitude, foundLongitude,0,0,0);
        
        return rl;
    
    }
}
