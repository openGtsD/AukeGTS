package no.auke.drone.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import no.auke.drone.application.TrackerUpdater;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/24/2015.
 */
public abstract class AbstractTrackerBase implements Tracker, Observer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTrackerBase.class);

    AtomicLong stopFlightTime = new AtomicLong(System.currentTimeMillis());
    AtomicLong startFlightTime = new AtomicLong(System.currentTimeMillis());

    private boolean active = true;

    protected AtomicBoolean ismoving = new AtomicBoolean(true); // default value
    
    private MapPoint currentPosition = new MapPoint();

    protected ReentrantLock block = new ReentrantLock();

    private String id;
    
    private String layerId;

    protected TrackerUpdater trackerUpdater;

    private TrackerType droneType = TrackerType.SIMULATED;

    private List<MapPoint> positions;
    private CircularFifoBuffer latestPositions;

    private String owner;

    private String name;

    private String description;

    private String contactInfo;

    private String simPhone;
    private String imeiNumber;

    private Date createDate;
    private Date modifiedDate;

    private Date lastUsed;

    private String imageUrl;

    private boolean storedTrips = true; // default value

    private String trackerPrefix;

    public String getTrackerPrefix() {
        return trackerPrefix;
    }

    public void setTrackerPrefix(String trackerPrefix) {
        this.trackerPrefix = trackerPrefix;
    }

    public AbstractTrackerBase() {
        latestPositions = new CircularFifoBuffer(5); // HUY: temporary set the latest positions at 5, will update it with parameter
    }

    public AbstractTrackerBase(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    @Override
    public Date getLastUsed() {
        return lastUsed;
    }

    @Override
    public void setLastUsed(Date date) {
        lastUsed = date;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    final public String getId() {
        return id;
    }

    @Override
    final public void setId(String id) {
        this.id = id;
    }

    @Override
    final public String getName() {
        return name;
    }

    @Override
    final public void setName(String name) {
        this.name = name;
    }

    @Override
    final public String getDescription() {
        return description;
    }

    @Override
    final public void setDescription(String description) {
        this.description = description;
    }

    @Override
    final public String getContactInfo() {
        return contactInfo;
    }

    @Override
    final public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
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
        this.currentPosition = currentPosition;
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

    @Override
    public boolean isStoredTrips() {
        return storedTrips;
    }

    @Override
    public void setStoredTrips(boolean storedTrips) {
        this.storedTrips = storedTrips;
    }

    // TODO implement later
    @Override
    public String getTrackerUsage() {
        return null;
    }

    // TODO implement later
    @Override
    public List<String> getAdditionalLayers() {
        return null;
    }

    // TODO implement later
    @Override
    public void addLayer(TrackerLayer trackerLayer) {

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

                
                // TODO should turn it back once the trip service finishes trackerUpdater.getTripService().saveTrip(this);
                
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
        return "{id: " + id + ", name:" + name + " layer:" + layerId +  " }";
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
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String getOwner() {
        return owner;
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

    @Override
    final public String getLayerId() {
        return layerId;
    }

    @Override
    final public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    @Override
    final public MapPoint getCurrentPosition() {
        return currentPosition;
    }

    @Override
    final public boolean isMoving() {
        return ismoving.get();
    }
    
    private boolean checkLogitude(double southWestLon, double northEastLon) {
        if(southWestLon < northEastLon)
        	return (currentPosition.getLongitude() <= northEastLon &&
        			currentPosition.getLongitude() >= southWestLon
        			);
    	
    	
        else {
        	
        	// time line
        	return currentPosition.getLongitude() >= southWestLon || currentPosition.getLongitude() <= northEastLon; 
        	
        }    	
    	
    }

    private boolean checkLatitude(double southWestLat, double northEastLat) {
    	
        if(southWestLat < northEastLat) {
        	
        	return  ( this.currentPosition.getLatitude() >= southWestLat && 
        			  this.currentPosition.getLatitude() <= northEastLat
        			);
        	
        } else {
        	
        	// over pole

        	return ( this.currentPosition.getLatitude() >= southWestLat || 
        			  this.currentPosition.getLatitude() <= northEastLat
        		   );            	
        }
            	
    	
    }    
    @Override
    public boolean withinView(double southWestLat, double southWestLon, double northEastLat, double northEastLon) {

    	// longitude are horizontal -> value -180 to 180
    	// latitude are vertical -> value -90 to 90
    	
        try {

            block.lock();

        	// LHA: fixed
            return checkLogitude(southWestLon,northEastLon) && checkLatitude(southWestLat,northEastLat);


        } finally {

            block.unlock();
        }

    }



    @Override
    public void setActive(boolean active) {
    	
        this.active = active;
    
        // if set passive, trip will be saved
        setMoving(active);

    }

    @Override
    public boolean isActive() {
        return active;
    }


    private void calculateSimulatedTrackers() {
        
    	if(isMoving()) {

            if((stopFlightTime.get() - System.currentTimeMillis())<0) {

                // stop flight
                logger.debug(this.toString() + "stop calculate");
                setMoving(false);

            } else {

                logger.trace(this.toString() + "started calculating");
                trackerUpdater.update(this);
                logger.debug(this.toString() + "finished calculating");

            }


        } else {

            if((startFlightTime.get() - System.currentTimeMillis())<0) {

                // stop flight
                logger.debug(this.toString() + "start calculate");
                setMoving(true);

            } else {

                logger.debug(this.toString() + "is not flying!!!");

            }

        }
    }

    @Override
    public void calculate() {
        
    	if(TrackerType.REAL.toString().equalsIgnoreCase(this.getLayerId())) {
        
        	logger.trace(this.toString() + "started calculating");
            trackerUpdater.update(this);
            logger.debug(this.toString() + "finished calculating");
        
        } else {
        
        	calculateSimulatedTrackers();
        }
    	
    }

    @Override
    public Tracker move(Integer speed, Integer course) {

        try {

            block.lock();

            if(logger.isDebugEnabled()) {
                logger.debug(this.toString() + "started moving");
            }

            // LHA:
            // maybe go slower

            Random ran = new Random();
            if(speed == null) {
                speed = 5;
            }
            //
            speed = speed + (ran.nextInt(1) - 1);
            if(speed<0){
                speed = -speed;
            }

            if(course == null) {
                course = 360;
            }
            //
            course = course + (ran.nextInt(1) - 1);
            if(course<0) {
                course=-course;
            }


            // fly
            double dx = speed * Math.sin(course);
            double dy = speed * Math.cos(course);
            double deltaLongitude = dx / (111320 * Math.sin(this.getCurrentPosition().getLatitude()));
            double deltaLatitude = dy / 110540;
            double finalLongitude = getCurrentPosition().getLongitude() + deltaLongitude;
            double finalLatitude = getCurrentPosition().getLatitude() + deltaLatitude;

            MapPoint positionUnit = new MapPoint(finalLatitude, finalLongitude, this.getCurrentPosition().getLatitude(), course, speed);

            setCurrentPosition(positionUnit);
            getLatestPositions().add(positionUnit);

            if(logger.isDebugEnabled())
                logger.debug(this.toString() + "finished moving");

            return this;

        } finally {

            block.unlock();
        }
        
        

    }
    
  
}
