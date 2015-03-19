package no.auke.drone.dao;

import java.util.ArrayList;
import java.util.List;

import no.auke.drone.domain.PositionUnit;

/**
 * @author thaihuynh
 * Using for test
 */
public class DummyCreator {
    static List<PositionUnit> list =  new ArrayList<PositionUnit>();
    
    static {
        PositionUnit unit1 = createPositionUnit("111", 1413835537, 38.646, -121.381, 7 ,0.0);
        PositionUnit unit2 = createPositionUnit("222", 1413835537, 38.638, -121.492, 5 ,0.0);
        PositionUnit unit3 = createPositionUnit("333", 1413835537, 38.575, -121.57, 5 ,0.0);
        PositionUnit unit4 = createPositionUnit("444", 1413835537, 38.557, -121.678, 5 ,0.0);
        PositionUnit unit5 = createPositionUnit("555", 1413835537, 38.515, -121.775, 10 ,0.0);
        
        list.add(unit1);
        list.add(unit2);
        list.add(unit3);
        list.add(unit4);
        list.add(unit5);
    }
    
    public DummyCreator(){
        
    }
        
    public List<PositionUnit> getAllTrack(){
        return list;
        
    }

    private static PositionUnit createPositionUnit(String id, long time, double lat, double lon, double altitude, double speed) {
        PositionUnit unit = new PositionUnit();
        unit.setId(id);
        unit.setTime(time);
        unit.setLat(lat);
        unit.setLon(lon);
        unit.setAltitude(altitude);
        unit.setSpeed(speed);
        return unit;
    }

    public List<PositionUnit> getTrackByGeo(double lat, double lon) {
        List<PositionUnit> result =  new ArrayList<PositionUnit>();
        for (PositionUnit positionUnit : list) {
            if(positionUnit.hasGeo(lat, lon)){
                result.add(positionUnit);
            }
        }
        return result;
    }
}
