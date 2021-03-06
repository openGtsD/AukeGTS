package no.auke.drone.application.impl;

import no.auke.drone.application.TrackerUpdater;
import no.auke.drone.domain.EventData;
import no.auke.drone.domain.MapPoint;
import no.auke.drone.domain.Tracker;
import no.auke.drone.services.EventService;
import no.auke.drone.services.TrackerService;
import no.auke.drone.services.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by huyduong on 4/27/2015.
 */
@Service
public class TrackerUpdaterImpl implements TrackerUpdater {
    private static final Logger logger = LoggerFactory.getLogger(TrackerUpdater.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private TripService tripService;

    @Override
    public void update(Tracker tracker) {
        if(logger.isDebugEnabled()) logger.debug("starting updating tracker " + tracker.getId());

        if(tracker.getLayerId().equalsIgnoreCase(Tracker.TrackerType.SIMULATED.toString())) {
        
        	tracker.move(null, null);// fly randomly
            // update to mappoint table


    	// } else if(tracker.getLayerId().equalsIgnoreCase(Tracker.TrackerType.REAL.toString())) {
    	} else {
    		// ALL other layers got position updated
    		
    		// LHA: Better use a map collection
    		Map<String,EventData> eventDatas = eventService.getEventDatas();
    		if(eventDatas.containsKey(tracker.getId())) {
    			tracker.setCurrentPosition(new MapPoint(eventDatas.get(tracker.getId())));
    			
    		}
        }
        
        // LHA: a trip is not one position, its a series of positions
        // 
        
        MapPoint currentPosition = tracker.getCurrentPosition();
        currentPosition.setTrackerId(tracker.getId());
        
        // LHA: by doing DB lookup for each time update is very resource consuming
        // we agreed this was to be in memmory
        
        if(tripService.getTripById(currentPosition.getId()) == null) {
            tripService.saveTrip(currentPosition);

        }
    }
}
