package no.auke.drone.application.impl;

import no.auke.drone.application.TrackerUpdater;
import no.auke.drone.domain.EventData;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Tracker;
import no.auke.drone.services.EventService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huyduong on 4/27/2015.
 */
@Service
public class TrackerUpdaterImpl implements TrackerUpdater {

    @Autowired
    private EventService eventService;

    @Override
    public void update(Tracker tracker) {
        if(tracker.getLayerId().equalsIgnoreCase(Tracker.TrackerType.SIMULATED.toString())) {
            tracker.move(null, null);// fly randomly
        } else if(tracker.getLayerId().equalsIgnoreCase(Tracker.TrackerType.REAL.toString())) {
            List<EventData> eventDatas = eventService.getEventDatas();
            if(CollectionUtils.isNotEmpty(eventDatas)) {
                for(EventData eventData : eventDatas) {
                    if(eventData.getDeviceID().equalsIgnoreCase(tracker.getId())) {
                        tracker.getLatestPositions().add(new MapPoint(eventData));
                    }
                }
            }
        }
    }
}
