package no.auke.drone.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

/**
 * Created by huyduong on 3/24/2015.
 */

public interface Tracker {
    
	public enum TrackerType {
        
    	REAL("Real"),
    	SIMULATED("Simulated"),
    	SUMMARIZED("summarized");
    
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

    String getId();
    
	void setId(String id);
    
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
    
    String getLayerId();
    
	void setLayerId(String layerid);

    List<MapPoint> getPositions();

    void setPositions(List<MapPoint> positions);

    boolean withinView(double upperLat, double upperLon, double lowerLat, double lowerLon);

    boolean isMoving();

    void setMoving(boolean isFlying);

    void calculate();

    Tracker move(Integer speed, Integer course);
    
    String getSimPhone();
    
    void setSimPhone(String simPhone);

    Date getCreateDate();
    
    void setCreateDate(Date date);
    
    Date getModifiedDate();
    
    void setModifiedDate(Date data);

	int getNumtrackers();

	void setNumtrackers(int numtrackers);

	void incrementTrackers();

	CircularFifoBuffer getLatestPositions();

	void setLatestPositions(CircularFifoBuffer latestPositions);


    
}
