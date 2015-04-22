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
    public void setFlying(boolean isFlying) {
    	super.setFlying(isFlying);
    	
    	if(isFlying()) {
    		
    		// max simulate time
    		stopFlightTime.set(System.currentTimeMillis() + (30 + rnd.nextInt(60 * 5)) * 1000);
    		
    	} else {
    		
    		// max simulate time
    		startFlightTime.set(System.currentTimeMillis() + (30 + rnd.nextInt(60 * 5)*1000));
    	}
    }
    
    
	@Override
    public void calculate() {
        
    	if(isFlying()) {
    		
    		if((stopFlightTime.get() - System.currentTimeMillis())<0) {
    			
    			// stop flight 
    			logger.info(this.toString() + "stop calculate");
    			setFlying(false);
    			
    		} else {

    			logger.trace(this.toString() + "started calculating");
                move(null,null);// fly randomly
                logger.debug(this.toString() + "finished calculating");
    			
    		}
        
        
    	} else {
    		
    		if((startFlightTime.get() - System.currentTimeMillis())<0) {

    			// stop flight 
    			logger.info(this.toString() + "start calculate");
    			setFlying(true);
    			
    		} else {

    			logger.debug(this.toString() + "is not flying!!!");
    			
    		}
            
        }
    }    

    @Override
    public Tracker move(Integer speed, Integer course) {

    	try {

    		block.lock();

        	logger.info(this.toString() + "started moving");
            Random ran = new Random();
            if(speed == null) {
                speed = 100 * (ran.nextInt(1) + 10);
            }

            if(course == null) {
                course = ran.nextInt(1) + 360;
            }

            // fly
            double dx = speed * Math.sin(course);
            double dy = speed * Math.cos(course);
            double deltaLongitude = dx / (111320 * Math.sin(getAltitude()));
            double deltaLatitude = dy / 110540;
            double finalLongitude = getCurrentPosition().getLongitude() + deltaLongitude;
            double finalLatitude = getCurrentPosition().getLatitude() + deltaLatitude;
            
            MapPoint positionUnit = new MapPoint(finalLatitude, finalLongitude, this.getAltitude(), course, speed);
            
            setCurrentPosition(positionUnit);
            getLatestPositions().add(positionUnit);
            
            logger.info(this.toString() + "finished moving");
            
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
	public void setNumtrackers(int numtrackers) {
	}

	@Override
	public void incrementTrackers() {
	}

}
