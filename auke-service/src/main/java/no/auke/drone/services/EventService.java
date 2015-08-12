package no.auke.drone.services;

import java.util.Map;

import no.auke.drone.entity.EventData;

/**
 * Created by huyduong on 4/24/2015.
 */
public interface EventService {
    void fetchEvents();
    Map<String,EventData> getEventDatas();
    void fetchEventData();
}
