package no.auke.drone.services.impl;

import java.util.List;

import no.auke.drone.dao.DummyCreator;
import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.PositionUnit;
import no.auke.drone.services.LiveTrackServices;

import org.springframework.stereotype.Service;

@Service
public class LiveTrackServicesImpl implements LiveTrackServices {

    // @Autowired
    // private PositionUnitDAO positionUnitDAO;

    public List<PositionUnit> loadTrackByGeo(double lat, double lon) {
        // return positionUnitDAO.getTrackByGeo(lat, lon);
        return new DummyCreator().getTrackByGeo(lat, lon);
    }

    public List<PositionUnit> loadAllTrack() {
        // return positionUnitDAO.getAllTrack();
        return new DummyCreator().getAllTrack();
    }

    @Override
    public PositionUnit getDrone(String droneId) {
        return new DummyCreator().getDrone(droneId);
    }

    // LHA: implement this
    @Override
    public List<PositionUnit> loadTrackWithinView(double upperLat, double upperLon, double lowerLat, double lowerLon) {
        return new DummyCreator().getTrackWithinView(upperLat, upperLon, lowerLat, lowerLon);
    }

    public List<PositionUnit> loadTrackWithinView(BoundingBox boundary) {
        return new DummyCreator().getTrackWithinView(boundary.getNorthEastLat(), boundary.getSouthWestLon(),
                boundary.getSouthWestLat(), boundary.getNorthEastLon());
    }

    @Override
    public List<PositionUnit> makeRandomDrones() {
        return new DummyCreator().getList();
    }

}
