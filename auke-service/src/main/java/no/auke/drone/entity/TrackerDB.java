package no.auke.drone.entity;

import no.auke.drone.domain.Tracker;

import java.util.Date;

/**
 * Created by huyduong on 7/30/2015.
 */
public class TrackerDB {

    private String id;

    private String name;

    private String description;

    private String contactInfo;

    private String owner;

    private String layer;

    private Long createDate;

    private Long modifiedDate;

    private Long lastUsed;

    private boolean storedTrips = true; // default value

    private boolean active = true; // default value

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public boolean isStoredTrips() {
        return storedTrips;
    }

    public void setStoredTrips(boolean storedTrips) {
        this.storedTrips = storedTrips;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TrackerDB() {

    }

    public TrackerDB from(Tracker tracker) {
        this.id = tracker.getId();
        this.name = tracker.getName();
        this.description = tracker.getDescription();
        this.contactInfo = tracker.getContactInfo();
        this.owner = tracker.getOwner();
        this.createDate = tracker.getCreateDate() != null ? tracker.getCreateDate().getTime()/1000 : null;
        this.modifiedDate = tracker.getModifiedDate() != null ? tracker.getModifiedDate().getTime()/1000 : null;
        this.layer = tracker.getLayerId();
        this.lastUsed = tracker.getLastUsed() != null ? tracker.getLastUsed().getTime()/1000 : null;
        this.storedTrips = tracker.isStoredTrips();
        this.active = tracker.isActive();
        return this;
    }
}
