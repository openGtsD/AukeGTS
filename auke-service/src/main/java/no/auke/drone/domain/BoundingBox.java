package no.auke.drone.domain;

import java.io.Serializable;

public class BoundingBox implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7714198134181756430L;

    private double southWestLat, southWestLon, northEastLat, northEastLon;

    public BoundingBox() {}

    public BoundingBox(double southWestLat, double southWestLon, double northEastLat, double northEastLon ) {
    	
    	this.southWestLat=southWestLat;
    	this.southWestLon=southWestLon;
    	this.northEastLat=northEastLat;
    	this.northEastLon=northEastLon;
    }

    public double getSouthWestLat() {
        return southWestLat;
    }

    public void setSouthWestLat(double southWestLat) {
        this.southWestLat = southWestLat;
    }

    public double getSouthWestLon() {
        return southWestLon;
    }

    public void setSouthWestLon(double southWestLon) {
        this.southWestLon = southWestLon;
    }

    public double getNorthEastLat() {
        return northEastLat;
    }

    public void setNorthEastLat(double northEastLat) {
        this.northEastLat = northEastLat;
    }

    public double getNorthEastLon() {
        return northEastLon;
    }

    public void setNorthEastLon(double northEastLon) {
        this.northEastLon = northEastLon;
    }
}
