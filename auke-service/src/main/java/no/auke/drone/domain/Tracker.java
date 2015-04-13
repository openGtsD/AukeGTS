package no.auke.drone.domain;

import java.util.List;

/**
 * Created by huyduong on 3/24/2015.
 */
public interface Tracker {
    
	public enum TrackerType {
        
    	REAL("Real"),
    	SIMULATED("Simulated");
    
    	private String value;

        TrackerType(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static TrackerType fromValue(String v) {
            for (TrackerType c: TrackerType.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            return TrackerType.REAL;
        }
    };

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

    void setTrackerType(TrackerType name);

    TrackerType getTrackerType();

    void setFlyer(Person person);

    Person getFlyer();

    void update();


    MapPoint getCurrentPosition();

    void setCurrentPosition(MapPoint positionUnit);
    
    String getLayerid();
    
	void setLayerid(String layerid);

    List<MapPoint> getPositions();

    void setPositions(List<MapPoint> positions);

    boolean withinView(double upperLat, double upperLon, double lowerLat, double lowerLon);

    boolean isFlying();

    void setFlying(boolean isFlying);

    void calculate();

    Tracker move(Integer speed, Integer course);

}
