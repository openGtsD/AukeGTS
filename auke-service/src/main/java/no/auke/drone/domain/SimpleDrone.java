package no.auke.drone.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

/**
 * Created by huyduong on 3/24/2015.
 */
public class SimpleDrone implements Drone, Observer {
    private String id;
    private String name;
    private long time;
    private double altitude;
    private double speed;
    private String droneType;
    private Person flyer;
    private boolean isUsedCamera;
    private MapPoint currentPosition;
    private List<MapPoint> positions;

    public SimpleDrone() {
        positions = new ArrayList<MapPoint>();
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

    public void setCurrentPosition(MapPoint currentPosition) {
        this.currentPosition = currentPosition;
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
        double deltaLongitude = dx / (111320 * Math.cos(getAltitude()));
        double deltaLatitude = dy / 110540;
        double finalLongitude = this.currentPosition.getLongitude() + deltaLongitude;
        double finalLatitude = this.currentPosition.getLatitude() + deltaLatitude;
        MapPoint positionUnit = new MapPoint(finalLatitude, finalLongitude);
        currentPosition = positionUnit;
        positions.add(currentPosition);
        System.out.println(this.toString() + "finished calculating");
    }

    @Override
    public boolean equals(Object obj) {
        Drone drone = (Drone) obj;
        return StringUtils.trim(this.id).equalsIgnoreCase(StringUtils.trimToEmpty(drone.getId()))
                && StringUtils.trim(this.name).equalsIgnoreCase(StringUtils.trimToEmpty(drone.getName()));
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "drone id: " + id + ", name:" + name + ", latitude " + currentPosition.getLatitude() + ", longitude"
                + currentPosition.getLongitude();
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;

    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public double getAltitude() {
        return altitude;
    }

    @Override
    public void setAltitude(double altitude) {
        this.altitude = altitude;

    }

    @Override
    public boolean isUsedCamera() {
        return isUsedCamera;
    }

    @Override
    public void setUsedCamera(boolean isUsedCamera) {
        this.isUsedCamera = isUsedCamera;
    }

    @Override
    public void setDroneType(String type) {
        this.droneType = type;
    }

    @Override
    public String getDroneType() {
        return this.droneType;
    }

    @Override
    public void setFlyer(Person person) {
        this.flyer = person;
    }

    @Override
    public Person getFlyer() {
        return flyer;
    }

    @Override
    public MapPoint getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public List<MapPoint> getPositions() {
        return positions;
    }

    @Override
    public void setPositions(List<MapPoint> positions) {
        this.positions = positions;
    }

    // LHA: something like this get position with a boundary
    @Override
    public boolean withinView(double upperLat, double upperLon, double lowerLat, double lowerLon) {
        return (this.currentPosition.getLongitude() <= upperLon && this.currentPosition.getLongitude() >= lowerLon)
                && (this.currentPosition.getLatitude() <= upperLat && this.currentPosition.getLatitude() >= lowerLat);
    }
}
