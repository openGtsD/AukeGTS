package com.auke.drone.ws.controllers;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;
import no.auke.drone.services.TrackerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auke.drone.ws.dto.JsonResponse;

/**
 * Created by huyduong on 3/24/2015.
 */
@Path("/drone")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class TrackerController {
    @Autowired
    private TrackerService trackerService;

    @GET
    @Path("/register")
    public JsonResponse register(@QueryParam("id") String id, @QueryParam("name") String name) {
        Tracker tracker = trackerService.registerTracker(id, name);
        return new JsonResponse(tracker == null, tracker);
    }

    @GET
    @Path("/remove")
    public JsonResponse remove(@QueryParam("id") String id, @QueryParam("name") String name) {
        Tracker tracker = trackerService.removeTracker(id);
        return new JsonResponse(tracker == null, tracker);
    }

    @GET
    @Path("/{id}")
    public JsonResponse get(@PathParam("id") String id) {
        Tracker tracker = trackerService.getTracker(id);
        return new JsonResponse(tracker != null, tracker);
    }

    @GET
    @Path("/get-all/{type}")
    public JsonResponse getAll(@PathParam("type") String trackerType) {
        Collection<Tracker> trackers = trackerService.getAll(trackerType);
        return new JsonResponse(trackers != null, trackers);
    }

    @GET
    @Path("/start/{id}")
    public JsonResponse start(@PathParam("id") String id) {
        Tracker tracker = trackerService.start(id);
        return new JsonResponse(tracker != null, tracker);
    }

    @GET
    @Path("/stop/{id}")
    public JsonResponse stop(@PathParam("id") String id) {
        Tracker tracker = trackerService.stop(id);
        return new JsonResponse(tracker != null, tracker);
    }
    
    @POST
    @Path("/load-drone-in-view/{layerId}/{zoom}")
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonResponse loadDroneWithinView(BoundingBox boundary, @PathParam("layerId") String layerId, @PathParam("zoom") int zoom) {
        Collection<Tracker> data = trackerService.loadWithinView(boundary, zoom, layerId);
        JsonResponse response = new JsonResponse(data != null, data);
        return response;
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonResponse update(Tracker tracker) {
        Tracker newTracker = trackerService.update(tracker);
        JsonResponse response = new JsonResponse(newTracker != null, newTracker);
        return response;
    }
}
