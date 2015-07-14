package no.auke.drone.domain;

import org.apache.commons.lang.StringUtils;

/**
 * Created by huyduong on 4/19/2015.
 */


public class Device {
    @ID
    private String deviceID;
    private String accountID;

    private String description;

    private String simPhoneNumber;

    private String uniqueID;

    private String imeiNumber;

    private Boolean isActive;

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
        simPhoneNumber = tracker.getSimPhone();
        imeiNumber = tracker.getImeiNumber();
        uniqueID = StringUtils.trimToEmpty(tracker.getTrackerPrefix()) + "-" + StringUtils.trimToEmpty(tracker.getImeiNumber());

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

    public String getSimPhoneNumber() {
        return simPhoneNumber;
    }

    public void setSimPhoneNumber(String simPhoneNumber) {
        this.simPhoneNumber = simPhoneNumber;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
