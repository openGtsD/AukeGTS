package no.auke.drone.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import no.auke.drone.annotation.Column;
import no.auke.drone.annotation.ID;
import no.auke.drone.domain.Tracker;
import no.auke.drone.domain.Tracker.TrackerType;
import no.auke.drone.utils.ByteUtil;

public class Trip implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @ID
    @Column
    private UUID id;

    @Column
    private String trackerId;
    
    @Column
	private String trackerName;

    @Column
    private String owner;

    @Column
    private String contactInfo;

    @Column
    private TrackerType trackerType;

    @Column
    private String tripName;

    // where did the trip took place
    @Column
    private String country;

    @Column
    private String city;

    // what is this trip done (update by user)
    @Column
    private String description;

    // trip is to be shown in public
    // @Column TODO to check later
    private boolean isPublic;


    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public TrackerType getTrackerType() {
        return trackerType;
    }

    public void setTrackerType(TrackerType trackerType) {
        this.trackerType = trackerType;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }    

    Tracker tracker;

    public Tracker getTracker() {
        return tracker;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    public String getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }

    public Trip() {
    };

    public long getStartTime() {

        if (route.size() > 0) {

            return route.get(0).getTime();

        } else {

            return 0;
        }

    }

    public long getStopTime() {
        if (route.size() > 0) {

            return route.get(route.size() - 1).getTime();

        } else {

            return 0;
        }
    }

    // startpoint is first on route
    public MapPoint getStartPoint() {

        if (route.size() > 0) {

            return route.get(0);

        } else {

            return new MapPoint();
        }

    }

    // stop is last on route
    public MapPoint getStopPoint() {

        if (route.size() > 0) {

            return route.get(route.size() - 1);

        } else {

            return new MapPoint();
        }

    }

    // get time of trip in seconds
    public int getTripTime() {

        if (route.size() > 0) {

            return (int) ((route.get(route.size() - 1).getTime() - route.get(0).getTime()) / 1000);

        } else {

            return 0;
        }

    }

    // get distance of trip in meter
    public double getDistanceMeter() {

        // TODO: getDistanceMeter, to be implemented

        if (route.size() > 0) {

            return 0;

        } else {

            return 0;
        }

    }

    // .... and more attributes to define

    // internal storage of trip points
    private List<MapPoint> route = new ArrayList<MapPoint>();

    public Trip(Tracker tracker) {
        this.trackerId = tracker.getId();
        this.tracker = tracker;
        id = UUID.randomUUID();
        setRoute(tracker.getPositions());
    }
    
    public Trip(TripInfo trip) {
        this.trackerId = tracker.getId();
        setRoute(trip.getRoute());
    }    

    public Trip(String trackerId, String tripid) {
        this.trackerId = trackerId;
        this.id = UUID.fromString(tripid);
    }

    public List<MapPoint> getRoute() {
        return route;
    }

    public void setRoute(List<MapPoint> positions) {
        route.clear();
        for (MapPoint point : positions) {
            route.add(point);
        }
    }

    // LHA: This pack route postions into a byte array for storing into database
    // in one field

    public byte[] getRouteAsBytes() {

        if (null == route || route.size() <= 0)
            return null;

        List<byte[]> positions = new ArrayList<byte[]>();
        for (MapPoint point : route) {
            positions.add(point.getBytes());
        }
        return ByteUtil.mergeDynamicBytesWithLength(positions);

    }

    // LHA: this pack out the byte array of positions and store them in the
    // route array
    public void setRouteAsBytes(byte[] data) {
        route.clear();
        List<byte[]> poslist = ByteUtil.splitDynamicBytes(data);
        for (byte[] pos : poslist) {
            route.add(new MapPoint(pos));
        }
    }

}
