package no.auke.drone.controller;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Device;
import no.auke.drone.domain.EventData;
import no.auke.drone.domain.SimpleTracker;
import no.auke.drone.domain.Tracker;
import no.auke.drone.dto.AukeResponse;
import no.auke.drone.services.EventService;
import no.auke.drone.services.TrackerService;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private CRUDDao<Device> crudDeviceDao;
    @Autowired
    private CRUDDao<EventData> crudEventDao;
    @Autowired
    private EventService eventService;

    @PostConstruct
    public void init() {
        crudEventDao.setPersistentClass(EventData.class);
        crudDeviceDao.setPersistentClass(Device.class);
    }

    @GET
    @Path("/get-all/{type:.*}")
    public AukeResponse getAll(@PathParam("type") String trackerType) {
        Collection<Tracker> trackers = trackerService.getAll(trackerType);
        return new AukeResponse(trackers != null, trackers);
    }

    @GET
    @Path("/start/{id}")
    public AukeResponse start(@PathParam("id") String id) {
        Tracker tracker = trackerService.start(id);
        return new AukeResponse(tracker != null, tracker);
    }

    @GET
    @Path("/stop/{id}")
    public AukeResponse stop(@PathParam("id") String id) {
        Tracker tracker = trackerService.stop(id);
        return new AukeResponse(tracker != null, tracker);
    }
    
    @POST
    @Path("/load-drone-in-view/{layerId}/{zoom}")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse loadDroneWithinView(BoundingBox boundary, @PathParam("layerId") String layerId, @PathParam("zoom") int zoom) {
        Collection<Tracker> data = trackerService.loadWithinView(boundary, zoom, layerId);
        AukeResponse response = new AukeResponse(data != null, data);
        return response;
    }

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

        List<EventData> events = eventService.getEventDatas();
        return new AukeResponse(events != null, events);
    }
    
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse register(@QueryParam("id")String id) {
        Tracker newTracker = trackerService.registerTracker(id, "");
        AukeResponse response = new AukeResponse(newTracker != null, newTracker);
        return response;
    }
    
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse remove(String id) {
        Tracker tracker = trackerService.removeTracker(id);
        return new AukeResponse(tracker != null, tracker);
    }
    
    @POST
    @Path("/get-tracker")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse getTracker(String id) {
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
    @Path("/updatetest")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse update(@QueryParam("id")String id) {
        Tracker tracker = trackerService.getTracker(id);
        Tracker newTracker = trackerService.update(tracker);
        AukeResponse response = new AukeResponse(newTracker != null, newTracker);
        return response;
    }

    @GET
    @Path("/deletetest")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse delete(@QueryParam("id")String id) {
        Tracker newTracker = trackerService.removeTracker(id);
        AukeResponse response = new AukeResponse(newTracker != null, newTracker);
        return response;
    }
}
