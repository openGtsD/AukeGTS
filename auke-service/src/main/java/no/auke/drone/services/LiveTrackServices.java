package no.auke.drone.services;

import java.util.List;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.PositionUnit;

public interface LiveTrackServices {
	
	// LHA: map view boundaries
	// lat and lon is the position of the left down corner of view
	// for ex.
	// posupper.lon,posupper.lat, postlower.lon,poslower.lat
    public List<PositionUnit> loadTrackWithinView(double upperLat, double upperLon, double lowerLat, double lowerLon );
    
    public List<PositionUnit> loadTrackByGeo(double lat, double lon);
    
    public List<PositionUnit> loadAllTrack();

    public PositionUnit getDrone(String droneId);

    public List<PositionUnit> loadTrackWithinView(BoundingBox boundary);

    public List<PositionUnit> makeRandomDrones();
        
}
