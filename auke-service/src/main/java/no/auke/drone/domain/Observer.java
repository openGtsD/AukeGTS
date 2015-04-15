package no.auke.drone.domain;

/**
 * Created by huyduong on 3/24/2015.
 */
public interface Observer {
    String getId();
    String getLayerid();
    void update();
}
