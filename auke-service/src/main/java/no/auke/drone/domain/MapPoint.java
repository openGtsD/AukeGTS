package no.auke.drone.domain;

import java.util.UUID;

public class MapPoint {
    @ID
    private String id;

    private long time;

    private double latitude;
    private double longitude;

    // Must have altitude to 
    private double altitude;

	// possibly direction
    private double course;
    
    // possibly speed
    private double speed;

    private String trackerId;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        Long timestamp = System.currentTimeMillis()/1000;
        this.id = UUID.randomUUID().toString();
        this.time = timestamp.intValue();
    }

    public MapPoint(double lat, double lon, double alt, Integer speed, double course) {
        this("",lat,lon,alt,speed,course);
    }

    public MapPoint(String trackerId, double lat, double lon, double alt, Integer speed, double course) {
        this();
        this.trackerId = trackerId;
    	this.latitude = lat;
        this.longitude = lon;
        this.altitude = alt;
        this.course = course;
        this.speed = speed;
    }

    public MapPoint(double lat, double lon) {
        this("",lat,lon,0,0,0);
        
    }

    public String getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }

    public MapPoint (EventData eventData) {
        this(eventData.getDeviceID(), eventData.getLatitude(), eventData.getLongitude(), eventData.getAltitude(), 0, eventData.getHeading());
        this.time = eventData.getTimestamp();

        // HUY: This is the format of time in OpenGTS DateTime dt = new DateTime(time); // GMT
        // located in EventUtil.java 631
    }

    public String toString() {
        return "{latitude: " + latitude + ", longitude:" + longitude + ", altitude:" + altitude + " ,time:" + time + "}";
    }
}
