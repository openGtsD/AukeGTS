package no.auke.drone.services;

import no.auke.drone.domain.EventData;

import java.util.List;
import java.util.Map;

/**
 * Created by huyduong on 4/24/2015.
 */
public interface EventService {
    void fetchEvents();
    Map<String,EventData> getEventDatas();
    void fetchEventData();
}
