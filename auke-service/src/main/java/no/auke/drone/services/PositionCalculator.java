package no.auke.drone.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.auke.drone.domain.Drone;
import no.auke.drone.domain.PositionUnit;

// 
// LHA: example of calculation thread
// 

public class PositionCalculator {

	private static final Logger logger = LoggerFactory.getLogger(PositionCalculator.class);

	protected static final long CALC_FREQUENCY = 10000; // time in milliseconds
	
	
	private AtomicBoolean isrunning = new AtomicBoolean();
	private Map<String,Drone> droneList = new ConcurrentHashMap<String,Drone>();

	
	private ExecutorService executor;
	private Map<String,PositionUnit> currentPositionList;
	
	public PositionCalculator(ExecutorService executor, Map<String,PositionUnit> currentPositionList) {
		this.executor=executor;
		this.currentPositionList=currentPositionList;
	}
	
	public void startCalculate(Drone drone) {
		
		
		if(logger.isDebugEnabled()) 
			logger.debug("start drone " + drone.getId() +  "" + drone.getName());
		
		// Add drone
		droneList.put(drone.getId(), drone);
		
		// start calc tread if not already started
		if(!isrunning.getAndSet(true)) {
			
			executor.execute(new Runnable() {

				
				@Override
				public void run() {
			
					long lastStarted = System.currentTimeMillis();
					
					isrunning.set(true);
					
					while(isrunning.get()) {

						if(logger.isDebugEnabled()) 
							logger.debug("run calc");

						for(Drone drone:droneList.values()) {
							
							if(isrunning.get()) {
								
								// calc and update current positions
								drone.calculate();
								currentPositionList.put(drone.getId(), drone.getCurrentPosition());
								
							} else {
								
								break;
							}
							
							
						}

						if(isrunning.get() && (System.currentTimeMillis() - lastStarted) < CALC_FREQUENCY ) {

							// sleep for rest time to CALC_FREQUENCY
							try {
								Thread.sleep(CALC_FREQUENCY - System.currentTimeMillis() - lastStarted);
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
		
		isrunning.set(false);
		
	}
	

}
