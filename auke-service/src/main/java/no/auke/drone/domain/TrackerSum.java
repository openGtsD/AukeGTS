package no.auke.drone.domain;

import java.util.Date;
import java.util.List;

import no.auke.drone.application.TrackerUpdater;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

public class TrackerSum extends TrackerPositionBase {

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
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public String getName() {
		return name;
	}

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
	public void setMoving(boolean ismoving) {}

	@Override
	public void calculate() {}

	@Override
	public Tracker move(Integer speed, Integer course) {
		return null;
	}
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

    @Override
    public void setTrackerUpdater(TrackerUpdater trackerUpdater) {

    }
}
