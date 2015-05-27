package no.auke.drone.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/24/2015.
 */

public class SimpleTracker extends TrackerBase {
    
	private static final Logger logger = LoggerFactory.getLogger(SimpleTracker.class);

	AtomicLong stopFlightTime = new AtomicLong(System.currentTimeMillis());  
	AtomicLong startFlightTime = new AtomicLong(System.currentTimeMillis());  
	
	Random rnd = new Random(System.nanoTime());
	
    public SimpleTracker() {
    	super();
    }

    public SimpleTracker(String id) {
    	super(id);
    }
    
    // LHA:
    // fly for a random period of time
    // wait for a random period of time
    // to make simulated flights
    @Override
    public void setMoving(boolean isFlying) {
    	super.setMoving(isFlying);
    	
    	if(isMoving()) {
    		
    		// max simulate time
    		stopFlightTime.set(System.currentTimeMillis() + (30 + rnd.nextInt(60 * 5)) * 1000);
    		
    	} else {
    		
    		// max simulate time
    		startFlightTime.set(System.currentTimeMillis() + (30 + rnd.nextInt(60 * 5)*1000));
    	}
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

            Random ran = new Random();
            if(speed == null) {
                speed = 25 * (ran.nextInt(1) + 10);
            }

            if(course == null) {
                course = ran.nextInt(1) + 360;
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

            if(logger
                    .isDebugEnabled())
                     logger.debug(this.toString() + "finished moving");
            
            return this;
    		
    	} finally {
    		
    		block.unlock();
    	}

    	
        
    }

	@Override
	public int getNumtrackers() {
		return 1;
	}

	@Override
	public void incrementTrackers() {
	}

}
