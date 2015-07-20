package no.auke.drone.domain.trips;

import java.util.List;
import java.util.UUID;

import no.auke.drone.domain.MapPoint;
import no.auke.drone.utils.ByteUtil;

// wrapper class for mappoint on a trip

public class TripPoint extends MapPoint {
	
	public TripPoint() {
		
	}
	
	public TripPoint(MapPoint point) {

		this.setTime(point.getTime());
		this.setAltitude(point.getAltitude());
		this.setLongitude(point.getLongitude());
		this.setLatitude(point.getLatitude());
		this.setCourse(point.getCourse());
		this.setSpeed(point.getSpeed());
		
		
	}
	
	public TripPoint(byte[] data){
		
		if(data!=null && data.length > 0 ) {	
			
			List<byte[]> subs = ByteUtil.splitDynamicBytes(data);
			
			this.setTime(ByteUtil.getLong(subs.get(0)));
			this.setAltitude(ByteUtil.getDouble(subs.get(1)));
			this.setLongitude(ByteUtil.getDouble(subs.get(2)));
			this.setLatitude(ByteUtil.getDouble(subs.get(3)));
			this.setCourse(ByteUtil.getDouble(subs.get(4)));
			this.setSpeed(ByteUtil.getDouble(subs.get(5)));
			
		}	

	}		
	
    public byte[] getBytes() {
    	
        return  ByteUtil.mergeDynamicBytesWithLength
                (
                    ByteUtil.getBytes(this.getTime()),
                    ByteUtil.getBytes(this.getAltitude()),
                	ByteUtil.getBytes(this.getLongitude()),
                	ByteUtil.getBytes(this.getLatitude()),
                	ByteUtil.getBytes(this.getCourse()),
                	ByteUtil.getBytes(this.getSpeed())                
                );
    }	
		

}