package no.auke.drone.services;

import java.util.List;

import no.auke.drone.domain.PositionUnit;

public interface LiveTrackServices {
    public List<PositionUnit> loadTrackByGeo(double lat, double lon);
    
    public List<PositionUnit> loadAllTrack();

    public PositionUnit getDrone(String droneId);
        
}
