package no.auke.drone.domain;

import java.util.Date;
import java.util.List;

import no.auke.drone.application.TrackerUpdater;

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

    boolean withinView(double southWestLat, double southWestLon, double northEastLat, double northEastLon);    

    boolean isMoving();

    void setMoving(boolean isFlying);

    void calculate();

    Tracker move(Integer speed, Integer course);

    String getImeiNumber();

    void setImeiNumber(String imeiNumber);

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

    void setTrackerUpdater(TrackerUpdater trackerUpdater);

	boolean isActive();

    void setActive(boolean active);

    String getTrackerPrefix();
    
    void setTrackerPrefix(String prefix);

}
