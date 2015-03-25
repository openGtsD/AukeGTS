package no.auke.drone.domain;


import java.util.List;

/**
 * Created by huyduong on 3/24/2015.
 */
public interface Drone {
    void setId(String id);
    String getId();
    void setName(String name);
    String getName();
    void update();
    void calculate();
    PositionUnit getCurrentPosition();
    void setCurrentPosition(PositionUnit positionUnit);
    List<PositionUnit> getPositions();
    void setPositions(List<PositionUnit> positions);
}
