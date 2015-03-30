package no.auke.drone.services;

import no.auke.drone.domain.Drone;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by huyduong on 3/26/2015.
 */
public interface PositionCalculator {
    void startCalculate();
    void stopCalculate();
}
