package no.auke.drone.domain;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.swing.text.Position;
import java.util.*;

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
        System.out.println(this.toString() + "started calculating");
        Random ran = new Random();
        int theta = ran.nextInt(1) + 180;
        int speed = 1000 * (ran.nextInt(1) + 100);

        // fly randomly
        double dx = speed * Math.sin(theta);
        double dy = speed * Math.sin(theta);
        double deltaLongitude = dx/(111320*Math.cos(this.currentPosition.getAltitude()));
        double deltaLatitude = dy/110540;
        double finalLongitude = this.currentPosition.getLon() + deltaLongitude;
        double finalLatitude = this.currentPosition.getLat() + deltaLatitude;

        // add new position
        PositionUnit positionUnit = new PositionUnit();
        positionUnit.setSpeed(speed);
        positionUnit.setId(currentPosition.getId());
        positionUnit.setAltitude(currentPosition.getAltitude());
        positionUnit.setLon(finalLongitude);
        positionUnit.setLat(finalLatitude);
        positionUnit.setTime(System.currentTimeMillis());
        positionUnit.setPurpose(currentPosition.getPurpose());
        positionUnit.setDroneType(currentPosition.getDroneType());
        positionUnit.setFlyer(currentPosition.getFlyer());
        positionUnit.setUsedCamera(currentPosition.isUsedCamera());
        currentPosition = positionUnit;
        positions.add(currentPosition);
        System.out.println(this.toString() + "finished calculating");
    }

    @Override
    public PositionUnit getCurrentPosition() {
        return this.currentPosition;
    }


    @Override
    public boolean equals(Object obj) {
        Drone drone = (Drone) obj;
        return StringUtils.trim(this.id).equalsIgnoreCase(StringUtils.trimToEmpty(((Drone) obj).getId())) &&
                StringUtils.trim(this.name).equalsIgnoreCase(StringUtils.trimToEmpty(((Drone) obj).getName()));
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
