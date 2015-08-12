package no.auke.drone.services.impl;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import no.auke.drone.domain.Tracker;
import no.auke.drone.services.PositionCalculatorService;
import no.auke.drone.services.ZoomLayerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/26/2015.
 */
public class PositionCalculatorServiceImpl implements PositionCalculatorService {
    
	private static final Logger logger = LoggerFactory.getLogger(PositionCalculatorService.class);

    public static long CALC_FREQUENCY = 5000; // time in milliseconds

    private AtomicBoolean isRunning = new AtomicBoolean();
    private AtomicBoolean isRunningAutomatically = new AtomicBoolean(true);
    private LayerServiceImpl layerService;
    private ExecutorService executor;

    public PositionCalculatorServiceImpl(ExecutorService executor, LayerServiceImpl trackerLayer, boolean isRunningAutomatically) {
        this.layerService = trackerLayer;
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
                        
                    	for(Tracker tracker : layerService.getActiveTrackers()) {
                            
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
                        
                        layerService.calculateZoomLayers();
                        

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
		for(ZoomLayerService serv:layerService.getZoomLayers()) {
			serv.clear();
		}
        isRunning.set(false);
    }
}
