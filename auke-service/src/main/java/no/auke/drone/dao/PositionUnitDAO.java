package no.auke.drone.dao;

import java.util.List;

import no.auke.drone.domain.PositionUnit;

public interface PositionUnitDAO {
    public List<PositionUnit> getAllTrack();

    public List<PositionUnit> getTrackByGeo(double lat, double lon);
}
