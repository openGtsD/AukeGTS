package no.auke.drone.services;

import java.util.List;

import no.auke.drone.domain.PositionUnit;

public interface LiveTrackServices {
	
	// LHA: map view boundaries
	// lat and lon is the position of the left down corner of view
	// for ex.
	// 
    public List<PositionUnit> loadTrackWithinView(double lat, double lon, int height, int width);
    
    public List<PositionUnit> loadTrackByGeo(double lat, double lon);
    
    public List<PositionUnit> loadAllTrack();

    public PositionUnit getDrone(String droneId);
        
}
