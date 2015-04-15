package no.auke.drone.domain;

import java.io.Serializable;

public class MapPoint implements Serializable {

    private static final long serialVersionUID = 1026799887818657928L;
    private long time;

    private double latitude;
    private double longitude;

    // Must have altitude to 
    private double altitude;

	// possibly direction
    private double course;
    
    // possibly speed
    private double speed;
    
    // number of trackers on this positions
    private int numtrackers;
    
    public int getNumtrackers() {
		return numtrackers;
	}

	public void setNumtrackers(int numtrackers) {
		this.numtrackers = numtrackers;
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

    
    public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getCourse() {
		return course;
	}

	public void setCourse(double course) {
		this.course = course;
	}

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public MapPoint() {
        this.time = System.currentTimeMillis();
    }

    public MapPoint(double lat, double lon, double alt, Integer speed, Integer course) {
        this();
    	this.latitude = lat;
        this.longitude = lon;
        this.altitude = alt;
        this.course = course;
        this.speed = speed;
    }

    public MapPoint(double lat, double lon) {
        this(lat,lon,0,0,0);
        
    }
    
    public MapPoint(double lat, double lon, int numtrackers) {
        this(lat,lon);
        this.numtrackers=numtrackers;
    }    

}
