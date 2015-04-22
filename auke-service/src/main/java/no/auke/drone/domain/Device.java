package no.auke.drone.domain;

import java.util.Date;

/**
 * Created by huyduong on 4/19/2015.
 */


public class Device {
    @ID
    private String deviceID;
    private String accountID;

    private String description;
//    private long lastUpdateTime;
//    private long creationTime;

    public Device() {
        accountID = "demo"; // temporary
//        lastUpdateTime = new Date().getTime();
//        creationTime = new Date().getTime();
    }

    public Device from(Tracker tracker) {
        deviceID = tracker.getId();
        description = tracker.getName();
        return this;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public long getLastUpdateTime() {
//        return lastUpdateTime;
//    }
//
//    public void setLastUpdateTime(long lastUpdateTime) {
//        this.lastUpdateTime = lastUpdateTime;
//    }
//
//    public long getCreationTime() {
//        return creationTime;
//    }
//
//    public void setCreationTime(long creationTime) {
//        this.creationTime = creationTime;
//    }
}
