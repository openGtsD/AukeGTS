package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Created by huyduong on 3/24/2015.
 */
public class SimpleDrone implements Drone, Observer{
    private String id;
    private String name;
    private PositionUnit currentPosition;
    private List<PositionUnit> positions;

    public SimpleDrone() {
        positions = new ArrayList<PositionUnit>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentPosition(PositionUnit currentPosition) {
        this.currentPosition = currentPosition;
    }

    public List<PositionUnit> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionUnit> positionUnits) {
        this.positions = positionUnits;
    }

    public void update() {
        calculate();
    }

    @Override
    public void calculate() {
        System.out.println(this.toString() + " calculating");
    }

    @Override
    public PositionUnit getCurrentPosition() {
        return this.currentPosition;
    }


    @Override
    public boolean equals(Object obj) {
        Drone drone = (Drone) obj;
        return StringUtils.trim(this.id).equalsIgnoreCase(StringUtils.trimToEmpty(drone.getId())) &&
                StringUtils.trim(this.name).equalsIgnoreCase(StringUtils.trimToEmpty(drone.getName()));
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "drone id: " + id + ", name:" + name + " ";
    }
}
