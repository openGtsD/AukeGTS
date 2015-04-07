package no.auke.drone.domain;

import java.util.List;

/**
 * Created by huyduong on 3/24/2015.
 */
public interface Tracker {
    void setId(String id);

    String getId();

    void setName(String name);

    String getName();

    long getTime();

    void setTime(long time);

    double getSpeed();

    void setSpeed(double speed);

    double getAltitude();

    void setAltitude(double sltitude);

    boolean isUsedCamera();

    void setUsedCamera(boolean isUsedCamera);

    void setDroneType(String name);

    String getDroneType();

    void setFlyer(Person person);

    Person getFlyer();

    void update();

    void calculate();

    MapPoint getCurrentPosition();

    void setCurrentPosition(MapPoint positionUnit);

    List<MapPoint> getPositions();

    void setPositions(List<MapPoint> positions);

    boolean withinView(double upperLat, double upperLon, double lowerLat, double lowerLon);

    boolean isFlying();

    void setFlying(boolean isFlying);
}
