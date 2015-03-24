package no.auke.drone.domain;


import java.util.List;

/**
 * Created by huyduong on 3/24/2015.
 */
public interface Drone {
    void update();
    void calculate();
    PositionUnit getCurrentPosition();
    List<PositionUnit> getPositions();
}
