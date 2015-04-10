package no.auke.drone.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerData;
import no.auke.drone.domain.TrackerLayer;
import no.auke.drone.services.PositionCalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/26/2015.
 */
public class PositionCalculatorImpl implements PositionCalculator {
    
	private static final Logger logger = LoggerFactory.getLogger(PositionCalculator.class);

    protected static final long CALC_FREQUENCY = 10000; // time in milliseconds

    private AtomicBoolean isRunning = new AtomicBoolean();
    private AtomicBoolean isRunningAutomatically = new AtomicBoolean(true);

    private ExecutorService executor;

    public PositionCalculatorImpl(ExecutorService executor, boolean isRunningAutomatically) {
        
    	this.executor = executor;
        this.isRunningAutomatically = new AtomicBoolean(isRunningAutomatically);
    }

    private Collection<Tracker> getTrackers() {
        return TrackerData.getInstance().getTrackers();
    }

    public void startCalculate() {
        
    	// start calc tread if not already started
        if(isRunningAutomatically.get() && !isRunning.getAndSet(true)) {
        
        	executor.execute(new Runnable() {
            
        		@Override
                public void run() {
                
        			long lastStarted = System.currentTimeMillis();
                    isRunning.set(true);

                    while(isRunning.get()) {
                        
                    	if(logger.isDebugEnabled()) logger.debug("run calc");

                        for(Tracker tracker : getTrackers()) {
                            
                        	if(isRunning.get()) {
                                logger.info("tracker " + tracker.getId() + " is calculating");
                        		// calc and update current positions
                                tracker.calculate();
                            
                            } else {
                                
                            	break;
                            }
                        }

                        if(isRunning.get() && (System.currentTimeMillis() - lastStarted) < CALC_FREQUENCY ) {
                        
                        	// sleep for rest time to CALC_FREQUENCY
                            try {
                                Thread.sleep(CALC_FREQUENCY - (System.currentTimeMillis() - lastStarted));
                            } catch (InterruptedException e) {
                            }
                            lastStarted = System.currentTimeMillis();
                        
                        }
                    }
                }

            });
        }
    }

    public void stopCalculate() {
        isRunning.set(false);
    }
}
