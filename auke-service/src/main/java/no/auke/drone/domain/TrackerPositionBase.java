package no.auke.drone.domain;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/24/2015.
 */
public abstract class TrackerPositionBase implements Tracker, Observer {

    private static final Logger logger = LoggerFactory.getLogger(TrackerPositionBase.class);

    private List<String> innerTrackers;

    protected AtomicBoolean ismoving = new AtomicBoolean(true); // default value
    
    private long time;
    private double altitude;
    private double speed;

    private MapPoint currentPosition = new MapPoint();

    protected ReentrantLock block = new ReentrantLock();

    // Thai Huynh: Some fields need update tracker
    private String id;
    private String layerId;

    public TrackerPositionBase() {
    }

    public TrackerPositionBase(String id) {
        this();
        this.id = id;
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
    final public String getLayerId() {
        return layerId;
    }

    @Override
    final public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public void setCurrentPosition(MapPoint currentPosition) {
        this.currentPosition = currentPosition;
    }  

    @Override
    public String toString() {

        return "tracker id: " + id + ", latitude " + currentPosition.getLatitude() + ", longitude"
                + currentPosition.getLongitude();
    }

    @Override
    final public long getTime() {
        return time;
    }

    @Override
    final public void setTime(long time) {
        this.time = time;

    }

    @Override
    final public double getSpeed() {
        return speed;
    }

    @Override
    final public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    final public double getAltitude() {
        return altitude;
    }

    @Override
    final public void setAltitude(double altitude) {
        this.altitude = altitude;

    }

    @Override
    final public MapPoint getCurrentPosition() {
        return currentPosition;
    }

    @Override
    final public boolean isMoving() {
        return ismoving.get();
    }
    
    @Override
    public void setMoving(boolean isMoving) {
    	this.ismoving.set(isMoving);
    }

    
    private boolean checkLogitude(double southWestLon, double northEastLon) {
    	
        if(southWestLon < northEastLon) 
    	
        	return (
        			
        			currentPosition.getLongitude() <= northEastLon && 
        			currentPosition.getLongitude() >= southWestLon
        			
        			);
    	
    	
        else {
        	
        	// timeline
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

    	// logitude are horizontal -> value -180 to 180
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
    public void setNumtrackers(int numtrackers) {
    }
    
    @Override
    public boolean checkIfPassive() {
    
    	// LHA: check if tracker dont have position measure for a
    	// period if time = is passive
        long timeout = 1 * 60 * 1000; // HUY: if it exceeds 1 minutes
        long deltaTime = System.currentTimeMillis()/1000 - currentPosition.getTime();
        if(deltaTime > timeout) {
            return true;
        }
    	return false;
    }


//    @Override
//    public void addInnerTrackers(Tracker tracker) {
//        if(innerTrackers == null) {
//            innerTrackers = new ArrayList<>();
//        }
//        innerTrackers.add(tracker.getId());
//    }
//
//    @Override
//    public List<String> getInnerTrackers() {
//        if(this.innerTrackers == null) {
//            this.innerTrackers = new ArrayList<>();
//        }
//        return this.innerTrackers;
//    }
}
