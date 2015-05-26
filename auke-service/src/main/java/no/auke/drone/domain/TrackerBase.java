package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import no.auke.drone.application.TrackerUpdater;
import no.auke.drone.utils.LocationFunction;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/24/2015.
 */

public abstract class TrackerBase extends TrackerPositionBase {

    private static final Logger logger = LoggerFactory.getLogger(TrackerBase.class);

    protected TrackerUpdater trackerUpdater;

    private TrackerType droneType = TrackerType.SIMULATED;
    
    private List<MapPoint> positions;
    private CircularFifoBuffer latestPositions;

    private Person flyer;
    private boolean isUsedCamera;


    // Thai Huynh: Some fields need update tracker
    private String name;
    private String simPhone;
    private String imeiNumber;

    private Date createDate;
    private Date modifiedDate;

    public TrackerBase() {
        super();
        latestPositions = new CircularFifoBuffer(5); // HUY: temporary set the latest positions at 5, will update it with parameter
    }

    public TrackerBase(String id) {
        super(id);
        latestPositions = new CircularFifoBuffer(5); // HUY: temporary set the latest positions at 5, will update with parameters
    }

    final public String getName() {
        return name;
    }

    final public void setName(String name) {
        this.name = name;
    }
    
    @Override
    final public List<MapPoint> getPositions() {
    	if(positions==null) {
    		positions = new LinkedList<>();
    	}
        return positions;
    }
    
    @Override
    final public void setCurrentPosition(MapPoint currentPosition) {
        super.setCurrentPosition(currentPosition);
        // set history
        getPositions().add(currentPosition);
    }    

    @Override
    final public void setPositions(List<MapPoint> positions) {
        this.positions = positions;
    }

    @Override
    final public CircularFifoBuffer getLatestPositions() {
        if (latestPositions == null) {
            latestPositions = new CircularFifoBuffer(5); // capacity of 5 latest
                                                         // positions
        }
        return latestPositions;
    }
    
    @Override
    final public void setLatestPositions(CircularFifoBuffer latestPositions) {
        this.latestPositions = latestPositions;
    }    

    public void update() {
        calculate();
    }
    
    //
    // Override because used to save positions
    //
    
    @Override
    public void setMoving(boolean isMoving) {

        if (!isMoving && this.ismoving.getAndSet(false)) {

            try {

                block.lock();
                logger.info("Save tracker " + getId());
                
                LocationFunction.writeLocationHistoryByDroneId(this.getId(), getPositions());

                // clear when save
                getPositions().clear();

            } finally {

                block.unlock();
            }

        } else {

            this.ismoving.set(true);
        }

    }    

    @Override
    public String toString() {

        return "{id: " + getId() + ", name:" + name + " layer:" + getLayerId() + " ,position: " + getCurrentPosition().toString() + " }";
    }
    

    @Override
    final public TrackerType getTrackerType() {
        return this.droneType;
    }
    
    @Override
    final public void setTrackerType(TrackerType type) {
        this.droneType = type;
    }

    @Override
    public void setFlyer(Person person) {
        this.flyer = person;
    }

    @Override
    public Person getFlyer() {
        return flyer;
    }

    @Override
    final public String getSimPhone() {
        return simPhone;
    }

    @Override
    final public void setSimPhone(String simPhone) {
        this.simPhone = simPhone;
    }

    @Override
    final public String getImeiNumber() {
        return imeiNumber;
    }

    @Override
    final public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public void setTrackerUpdater(TrackerUpdater trackerUpdater) {
        this.trackerUpdater = trackerUpdater;
    }
    

}
