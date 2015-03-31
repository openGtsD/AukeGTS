package no.auke.drone.services.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import no.auke.drone.services.PositionCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.auke.drone.domain.Drone;
import no.auke.drone.domain.PositionUnit;

/**
 * Created by huyduong on 3/26/2015.
 */
public class PositionCalculatorImpl implements PositionCalculator {
    private static final Logger logger = LoggerFactory.getLogger(PositionCalculator.class);

    protected static final long CALC_FREQUENCY = 10000; // time in milliseconds

    private AtomicBoolean isRunning = new AtomicBoolean();
    private AtomicBoolean isRunningAutomatically = new AtomicBoolean(true);

    private Map<String,Drone> drones;

    private ExecutorService executor;

    public PositionCalculatorImpl(ExecutorService executor, Map<String,Drone> drones, boolean isRunningAutomatically) {
        this.executor = executor;
        this.drones = drones;
        this.isRunningAutomatically = new AtomicBoolean(isRunningAutomatically);
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

                        for(Drone drone: drones.values()) {
                            if(isRunning.get()) {
                                // calc and update current positions
                                drone.calculate();
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
