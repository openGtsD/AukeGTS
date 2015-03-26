package no.auke.drone.services.impl;

import java.util.List;

import no.auke.drone.dao.DummyCreator;
import no.auke.drone.domain.PositionUnit;
import no.auke.drone.services.LiveTrackServices;

import org.springframework.stereotype.Service;

@Service
public class LiveTrackServicesImpl implements LiveTrackServices {
    
//    @Autowired
//    private PositionUnitDAO positionUnitDAO;

    public List<PositionUnit> loadTrackByGeo(double lat, double lon) {
//        return positionUnitDAO.getTrackByGeo(lat, lon);
        return new DummyCreator().getTrackByGeo(lat, lon);
    }

    public List<PositionUnit> loadAllTrack() {
//        return positionUnitDAO.getAllTrack();
        return new DummyCreator().getAllTrack();
    }

    @Override
    public PositionUnit getDrone(String droneId) {
        return new DummyCreator().getDrone(droneId);
    }

}
