package no.auke.drone.domain;

import java.util.Random;

/**
 * Created by huyduong on 3/24/2015.
 */

public class SimpleTracker extends AbstractTrackerBase {
    

	Random rnd = new Random(System.nanoTime());

    public SimpleTracker() {
    	super();
    }

    public SimpleTracker(String id, String name) {
    	super(id, name);
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
            startFlightTime.set(System.currentTimeMillis() + (30 + rnd.nextInt(60 * 5) * 1000));
        }
    }
}
