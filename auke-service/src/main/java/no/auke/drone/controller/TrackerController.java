package no.auke.drone.controller;

import java.util.Collection;

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
import no.auke.drone.domain.*;
import no.auke.drone.services.TrackerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import no.auke.drone.dto.AukeResponse;

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

    @PostConstruct
    public void init() {
        crudEventDao.setPersistentClass(EventData.class);
        crudDeviceDao.setPersistentClass(Device.class);
    }

    @POST
    @Path("/remove/{id}")
    public AukeResponse remove(@PathParam("id") String id) {
        Tracker tracker = trackerService.removeTracker(id);
        return new AukeResponse(tracker != null, tracker);
    }

    @GET
    @Path("/{id}")
    public AukeResponse get(@PathParam("id") String id) {
        Tracker tracker = trackerService.getTracker(id);
        return new AukeResponse(tracker != null, tracker);
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

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse update(SimpleTracker tracker) {
        Tracker newTracker = trackerService.update(tracker);
        AukeResponse response = new AukeResponse(newTracker != null, newTracker);
        return response;
    }

    @GET
    @Path("/get-all-event")
    public AukeResponse getAll() {
        Collection<EventData> events = crudEventDao.getAll();
        return new AukeResponse(events != null, events);
    }
}
