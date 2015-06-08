package no.auke.drone.domain;

/**
 * Created by huyduong on 6/5/2015.
 */
public interface User {
    String getFirstName();

    String getLastName();

    String getFullName();

    String getEmail();

    Avatar getAvatar();

    Location getLocation();

    boolean isAdmin();
}
