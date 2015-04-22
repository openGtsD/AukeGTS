package no.auke.drone.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

public class TrackerSum extends TrackerPositionBase {

    private String id="";
    private String name="";	
	
	public TrackerSum() {}

    // number of trackers on this positions
    private int numtrackers;
    
	@Override
	public int getNumtrackers() {
		return numtrackers;
	}

	@Override
	public void setNumtrackers(int numtrackers) {
		this.numtrackers = numtrackers;
	}
	
	@Override
	public void incrementTrackers() {
		this.numtrackers++;
	}	

	@Override
	public TrackerType getTrackerType() {
		return TrackerType.SUMMARIZED;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getTime() {
		return 0;
	}

	@Override
	public void setTime(long time) {
	}

	@Override
	public double getSpeed() {
		return 0;
	}

	@Override
	public void setSpeed(double speed) {
	}

	@Override
	public double getAltitude() {
		return 0;
	}

	@Override
	public void setAltitude(double sltitude) {}

	@Override
	public boolean isUsedCamera() {
		return false;
	}

	@Override
	public void setUsedCamera(boolean isUsedCamera) {}

	@Override
	public void setTrackerType(TrackerType name) {}

	@Override
	public void setFlyer(Person person) {}

	@Override
	public Person getFlyer() {
		return null;
	}

	@Override
	public void update() {}


	@Override
	public void setCurrentPosition(MapPoint positionUnit) {}

	@Override
	public List<MapPoint> getPositions() {
		return null;
	}

	@Override
	public void setPositions(List<MapPoint> positions) {}

	@Override
	public boolean isFlying() {
		return false;
	}

	@Override
	public void setFlying(boolean isFlying) {}

	@Override
	public void calculate() {}

	@Override
	public Tracker move(Integer speed, Integer course) {
		return null;
	}

	@Override
	public String getImei() {
		return null;
	}

	@Override
	public void setImei(String imei) {}

	@Override
	public String getSimPhone() {
		return null;
	}

	@Override
	public void setSimPhone(String simPhone) {}

	@Override
	public Date getCreateDate() {
		return null;
	}

	@Override
	public void setCreateDate(Date date) {}

	@Override
	public Date getModifiedDate() {
		return null;
	}

	@Override
	public void setModifiedDate(Date data) {}

	@Override
	public CircularFifoBuffer getLatestPositions() {
		return null;
	}

	@Override
	public void setLatestPositions(CircularFifoBuffer latestPositions) {}

}
