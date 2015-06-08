package no.auke.drone.domain;

/**
 * Created by huyduong on 6/8/2015.
 */
public class Location {
    private String city;

    private String district;

    private String state;

    private String country;

    private String postalCode;

    private MapPoint coordinates;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public MapPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(MapPoint coordinates) {
        this.coordinates = coordinates;
    }
}
