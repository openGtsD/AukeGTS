package no.auke.drone.domain;

// Rename class later
public class PositionUnit {
    private String id;
    private long time;
    private double lat;
    private double lon;
    private double altitude; 
    private double speed;
    private String droneType;
    private Person flyer;
    private String purpose;
    private boolean isUsedCamera;
    
    public PositionUnit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public boolean hasGeo(double lat2, double lon2) {
        return this.lat == lat2 && this.lon == lon2;
    }

    public String getDroneType() {
        return droneType;
    }

    public void setDroneType(String droneType) {
        this.droneType = droneType;
    }

    public Person getFlyer() {
        return flyer;
    }

    public void setFlyer(Person flyer) {
        this.flyer = flyer;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public boolean isUsedCamera() {
        return isUsedCamera;
    }

    public void setUsedCamera(boolean isUsedCamera) {
        this.isUsedCamera = isUsedCamera;
    }

}
