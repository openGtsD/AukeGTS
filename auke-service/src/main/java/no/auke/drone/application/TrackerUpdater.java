package no.auke.drone.application;

import no.auke.drone.domain.Tracker;
import no.auke.drone.services.TripService;

/**
 * Created by huyduong on 4/27/2015.
 */
public interface TrackerUpdater {
    void update(Tracker tracker);
    TripService getTripService();
}
