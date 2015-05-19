package no.auke.drone.domain;

/**
 * Created by huyduong on 4/20/2015.
 */
public class EventData {
    @ID
    private String accountID;

    @ID
    private String deviceID;

    @ID
    private Integer statusCode;

    @ID
    private Integer timestamp;

    private double latitude;
    private double longitude;
    private double altitude;
    private double heading;
    private Long creationTime;

    public EventData() {
        Long timestamp = System.currentTimeMillis()/1000;
        this.timestamp = timestamp.intValue();
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
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

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        return "EventData {accountID:" + accountID + ", deviceID:" + deviceID + ", statusCode:" + statusCode + " ,timestamp:" + timestamp + " ,latitude:" + latitude + " ,longitude:" + longitude + " ,altitude:" + altitude + "}";
    }
}
