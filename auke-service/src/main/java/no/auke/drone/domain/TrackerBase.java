package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import no.auke.drone.utils.LocationFunction;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/24/2015.
 */
public abstract class TrackerBase extends TrackerPositionBase {

    private static final Logger logger = LoggerFactory.getLogger(TrackerBase.class);

    private TrackerType droneType = TrackerType.SIMULATED;
    
    private List<MapPoint> positions;
    private CircularFifoBuffer latestPositions;

    private Person flyer;
    private boolean isUsedCamera;


    // Thai Huynh: Some fields need update tracker
    private String name;
    private String imei;
    private String simPhone;
    private Date createDate;
    private Date modifiedDate;

    public TrackerBase() {
    	super();
    }

    public TrackerBase(String id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public List<MapPoint> getPositions() {
    	if(positions==null) {
    		positions = new ArrayList<MapPoint>();
    	}
        return positions;
    }
    
    public void setCurrentPosition(MapPoint currentPosition) {
        super.setCurrentPosition(currentPosition);
        // set history
        getPositions().add(currentPosition);
    }    

    @Override
    public void setPositions(List<MapPoint> positions) {
        this.positions = positions;
    }

    @Override
    public CircularFifoBuffer getLatestPositions() {
        if (latestPositions == null) {
            latestPositions = new CircularFifoBuffer(5); // capacity of 5 latest
                                                         // positions
        }
        return latestPositions;
    }
    @Override
    public void setLatestPositions(CircularFifoBuffer latestPositions) {
        this.latestPositions = latestPositions;
    }    

    public void update() {
        calculate();
    }

    @Override
    public String toString() {

        return "drone id: " + getId() + ", name:" + name + ", latitude " + getCurrentPosition().getLatitude() + ", longitude"
                + getCurrentPosition().getLongitude();
    }

    @Override
    public boolean isUsedCamera() {
        return isUsedCamera;
    }

    @Override
    public void setUsedCamera(boolean isUsedCamera) {
        this.isUsedCamera = isUsedCamera;
    }

    @Override
    public void setTrackerType(TrackerType type) {
        this.droneType = type;
    }

    @Override
    public TrackerType getTrackerType() {
        return this.droneType;
    }

    @Override
    public void setFlyer(Person person) {
        this.flyer = person;
    }

    @Override
    public Person getFlyer() {
        return flyer;
    }


    //
    // Override because used to save positions
    //
    
    @Override
    public void setMoving(boolean isFlying) {

        if (!isFlying && this.ismoving.getAndSet(false)) {

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSimPhone() {
        return simPhone;
    }

    public void setSimPhone(String simPhone) {
        this.simPhone = simPhone;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
