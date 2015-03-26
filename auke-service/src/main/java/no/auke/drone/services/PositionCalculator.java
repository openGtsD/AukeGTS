package no.auke.drone.services;

import no.auke.drone.domain.Drone;

/**
 * Created by huyduong on 3/26/2015.
 */
public interface PositionCalculator {
    void startCalculate();
    void stopCalculate();
}
