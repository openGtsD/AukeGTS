package no.auke.drone.controller;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.*;
import no.auke.drone.dto.AukeResponse;
import no.auke.drone.services.EventService;
import no.auke.drone.services.TrackerService;
import no.auke.drone.services.ZoomLayerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Created by huyduong on 3/24/2015.
 */
@Path("/drone")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class TrackerController {
    @Autowired
    private TrackerService trackerService;
    @Autowired
    private EventService eventService;

    private static final Logger logger = LoggerFactory.getLogger(TrackerController.class);

    @POST
    @Path("/load-drone-in-view/{layerId}/{zoom}")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse loadDroneWithinView(BoundingBox boundary, @PathParam("layerId") String layerId, @PathParam("zoom") int zoom) {
        logger.info("loading drones within view : " + layerId + " " + zoom);
        Collection<Tracker> data = trackerService.loadWithinView(boundary, zoom, layerId);
        AukeResponse response = new AukeResponse(data != null, data);
        return response;
    }

    
    //
    // LHA: What is this for ?
    // Event service only hold current event for all trackers active
    // To get history, it should be stored on the tracker
    
    // THAI: just only debugs real tracker send position correct ?
    @GET
    @Path("/get-event")
    public AukeResponse getEvent(@QueryParam("accountID")String accountID, @QueryParam("deviceID")String deviceID) {
       
    	Properties properties = new Properties();
        
    	if(StringUtils.isNotEmpty(accountID)) {
            properties.put("accountID", accountID);
        }

        if(StringUtils.isNotEmpty(deviceID)) {
            properties.put("deviceID", deviceID);
        }

        List<EventData> events = new ArrayList<EventData>(eventService.getEventDatas().values());
        return new AukeResponse(events != null, events);
    }
    
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse registerTracker(SimpleTracker tracker) {
        Tracker newTracker = trackerService.registerTracker(tracker);
        AukeResponse response = new AukeResponse(newTracker != null, newTracker);
        return response;
    }

    @POST
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse remove(@PathParam("id") String id) {
        Tracker tracker = trackerService.remove(id);
        return new AukeResponse(tracker != null, tracker);
    }
    
    @POST
    @Path("/get-tracker/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse getTracker(@PathParam("id") String id) {
        Tracker tracker = trackerService.getTracker(id);
        return new AukeResponse(tracker != null, tracker);
    }
    
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse update(SimpleTracker tracker) {
        Tracker newTracker = trackerService.update(tracker);
        AukeResponse response = new AukeResponse(newTracker != null, newTracker);
        return response;
    }

    @GET
    @Path("/createlayer")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse createTrackerLayer(@QueryParam("name")String name) {
        String layerName = trackerService.getTrackerLayer(name).getLayerName();
        return new AukeResponse(layerName != null, layerName);
    }

}
