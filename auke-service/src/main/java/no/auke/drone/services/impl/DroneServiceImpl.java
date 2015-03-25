package no.auke.drone.services.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import no.auke.drone.domain.Drone;
import no.auke.drone.domain.PositionUnit;
import no.auke.drone.domain.SimpleDrone;
import no.auke.drone.services.DroneService;
import no.auke.drone.services.PositionCalculator;

/**
 * Created by huyduong on 3/25/2015.
 */



// 
// LHA: look over. I am nott sure i follow you pattern, so you just
// refactor if needed

// important part: 

// current position list is the list where the map web service is to pull positions from
// must be a concurrent list to handle the threading without craching



public class DroneServiceImpl implements DroneService {
	
	Map<String,PositionUnit> currentPositionList = new ConcurrentHashMap<String,PositionUnit>();
		
	PositionCalculator positionCalculator;
	private ExecutorService executor = Executors.newCachedThreadPool(); 
	public ExecutorService getExecutor() {
		if(executor==null) {
			executor = Executors.newCachedThreadPool();
		}
		return executor;
	}
	
	public DroneServiceImpl() {
		
		positionCalculator = new PositionCalculator(getExecutor(),currentPositionList);
	
	}
	
	
	// LHA: not sure if use like this.
    @Override
    public void registerDrone() {
    	
    	// lha: anyway. Pull calculation to the drone
    	positionCalculator.startCalculate(createDrone());

    }

    @Override
    public void removeDrone() {

    }

    @Override
    public Drone createDrone() {
        return new SimpleDrone();
    }
}
