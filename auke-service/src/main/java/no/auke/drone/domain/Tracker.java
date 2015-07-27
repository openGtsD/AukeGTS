package no.auke.drone.domain;

import java.net.URL;
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

    void setDescription(String description);

    String getDescription();

    void setContactInfo(String contactInfo);

    String getContactInfo();

    void setTrackerType(TrackerType name);

    TrackerType getTrackerType();

    void setOwner(Person owner);

    Person getOwner();

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
    
    void setModifiedDate(Date date);

    Date getLastUsed();

    void setLastUsed(Date date);

    CircularFifoBuffer getLatestPositions();

	void setLatestPositions(CircularFifoBuffer latestPositions);

    void setTrackerUpdater(TrackerUpdater trackerUpdater);

	boolean isActive();

    void setActive(boolean active);

    String getImageUrl();

    void setImageUrl(String imageUrl);

    boolean isStoredTrips();

    void setStoredTrips(boolean storedTrips);

    String getTrackerUsage();

    List<String> getAdditionalLayers();

    void addLayer(TrackerLayer trackerLayer);

    void setTrackerPrefix(String trackerPrefix);

    String getTrackerPrefix();
}
