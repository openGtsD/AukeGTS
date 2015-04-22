package no.auke.drone.domain;

import java.util.Date;
import java.util.List;

public class TrackerSum implements Tracker {

    private MapPoint currentPosition;
    private String id="";
    private String name="";	
	
	public TrackerSum() {
		currentPosition=new MapPoint();
	}

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
	public MapPoint getCurrentPosition() {
		return currentPosition;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTime(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSpeed(double speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getAltitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAltitude(double sltitude) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUsedCamera() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setUsedCamera(boolean isUsedCamera) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTrackerType(TrackerType name) {
	}


	@Override
	public void setFlyer(Person person) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Person getFlyer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setCurrentPosition(MapPoint positionUnit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLayerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLayerId(String layerid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<MapPoint> getPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPositions(List<MapPoint> positions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean withinView(double upperLat, double upperLon,
			double lowerLat, double lowerLon) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFlying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFlying(boolean isFlying) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Tracker move(Integer speed, Integer course) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImei() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setImei(String imei) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSimPhone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSimPhone(String simPhone) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getCreateDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCreateDate(Date date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getModifiedDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModifiedDate(Date data) {
		// TODO Auto-generated method stub
		
	}

}
