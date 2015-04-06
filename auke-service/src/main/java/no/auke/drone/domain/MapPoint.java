package no.auke.drone.domain;

import java.io.Serializable;

public class MapPoint implements Serializable {

    private static final long serialVersionUID = 1026799887818657928L;
    
    private double latitude;
    private double longitude;

    public MapPoint(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
