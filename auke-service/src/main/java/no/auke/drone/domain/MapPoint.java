package no.auke.drone.domain;

public class MapPoint implements Point {
    
    private double latitude;
    private double longitude;
    
    public MapPoint(double lat, double lon) {
        this.latitude =  lat;
        this.longitude = lon;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

}
