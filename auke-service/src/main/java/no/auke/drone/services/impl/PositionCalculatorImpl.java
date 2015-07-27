package no.auke.drone.services.impl;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.TrackerLayer;
import no.auke.drone.services.PositionCalculator;
import no.auke.drone.services.ZoomLayerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/26/2015.
 */
public class PositionCalculatorImpl implements PositionCalculator {
    
	private static final Logger logger = LoggerFactory.getLogger(PositionCalculator.class);

    public static long CALC_FREQUENCY = 5000; // time in milliseconds

    private AtomicBoolean isRunning = new AtomicBoolean();
    private AtomicBoolean isRunningAutomatically = new AtomicBoolean(true);
    private TrackerLayer trackerLayer;
    private ExecutorService executor;

    public PositionCalculatorImpl(ExecutorService executor, TrackerLayer trackerLayer, boolean isRunningAutomatically) {
        this.trackerLayer = trackerLayer;
    	this.executor = executor;
        this.isRunningAutomatically = new AtomicBoolean(isRunningAutomatically);
    }

    public void startCalculate() {
        
    	logger.info("starting calculating" + isRunningAutomatically + isRunning);
    	
    	// start calc tread if not already started
        if(isRunningAutomatically.get() && !isRunning.getAndSet(true)) {
        
        	executor.execute(new Runnable() {
            
        		@Override
                public void run() {
                
        			long lastStarted = System.currentTimeMillis();
                    while(isRunning.get()) {
                        
                    	if(logger.isDebugEnabled()) 
                    		logger.debug("run calc");
                        
                    	for(Tracker tracker : trackerLayer.getActiveTrackers()) {
                            
                        	if(isRunning.get()) {
                                if(logger.isDebugEnabled()){
                                    logger.debug("tracker " + tracker.getId() + " is calculating");
                                }

                        		// calc and update current positions
                                tracker.calculate();
                            
                            } else {
                                
                            	break;
                            }
                        	Thread.yield();
                        }
                        
                        if(logger.isDebugEnabled()) 
                        	logger.debug("run calc zoomlevels");
                        
                        trackerLayer.calculateZoomLayers();
                        

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
		for(ZoomLayerService serv:trackerLayer.getZoomLayers()) {
			serv.clear();
		}
        isRunning.set(false);
    }
}
