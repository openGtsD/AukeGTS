package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import no.auke.drone.utils.LocationFunction;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/24/2015.
 */

public class SimpleTracker extends TrackerBase {
    
	private static final Logger logger = LoggerFactory.getLogger(SimpleTracker.class);

    public SimpleTracker() {
    	super();
    }

	@Override
    public void calculate() {
        
    	if(isFlying.get()) {
        
    		logger.info(this.toString() + "started calculating");
            move(null,null);// fly randomly
            logger.info(this.toString() + "finished calculating");
        
    	} else {
            
    		logger.info(this.toString() + "is not flying!!!");
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
                course = ran.nextInt(1) + 180;
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
            
            // LHA: move to stop drone
            // everytime you move is to heavy
            
            //LocationFunction.writeLocationHistoryByDroneId(id,currentPosition);
            // TODO this would use an asynchronous method so it is easier to move
            
            logger.info(this.toString() + "finished moving");
            
            return this;
    		
    		
    	} finally {
    		
    		block.unlock();
    	}

    	
        
    }


}
