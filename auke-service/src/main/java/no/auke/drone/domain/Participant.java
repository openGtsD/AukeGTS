package no.auke.drone.domain;

import java.util.Date;

/**
 * Created by huyduong on 6/8/2015.
 */
public interface Participant {
    
    public Date getDoB();

    
    public String getGender();

    
    public String getFirstName();
    
    public String getLastName();

    
    public String getFullName();

    
    public String getEmail();

    
    public Avatar getAvatar();

    
    public Location getLocation();

    
    public boolean isAdmin();
}
