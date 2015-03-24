package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyduong on 3/24/2015.
 */
public class SimpleDrone implements Drone, Observer{
    private PositionUnit positionUnit;
    private List<PositionUnit> positionUnits;

    public SimpleDrone() {
        positionUnits = new ArrayList<PositionUnit>();
    }

    public void update() {

    }

    @Override
    public void calculate() {

    }

    @Override
    public PositionUnit getCurrentPosition() {
        return this.positionUnit;
    }

    @Override
    public List<PositionUnit> getPositions() {
        return this.positionUnits;
    }
}
