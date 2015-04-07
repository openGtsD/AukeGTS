package com.auke.drone.ws.controllers;

import java.util.Collection;
import java.util.List;

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
    @Path("/getall")
    public JsonResponse getAll() {
        Collection<Tracker> trackers = trackerService.getAll();
        return new JsonResponse(trackers != null, trackers);
    }

    @GET
    @Path("/move")
    public JsonResponse move(@QueryParam("id") String id) {
        Tracker tracker = trackerService.move(id);
        return new JsonResponse(tracker == null, tracker);
    }

    @GET
    @Path("/start")
    public JsonResponse start(@QueryParam("id") String id) {
        Tracker tracker = trackerService.start(id);
        return new JsonResponse(tracker == null, tracker);
    }

    @GET
    @Path("/stop")
    public JsonResponse stop(@QueryParam("id") String id) {
        Tracker tracker = trackerService.stop(id);
        return new JsonResponse(tracker == null, tracker);
    }
    
    @POST
    @Path("/load-drone-in-view")
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonResponse loadDroneWithinView(BoundingBox boundary) {
        List<Tracker> data = trackerService.loadWithinView(boundary);
        JsonResponse response = new JsonResponse(data != null, data);
        return response;
    }
}
