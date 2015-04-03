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
import no.auke.drone.domain.Drone;
import no.auke.drone.services.DroneService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auke.drone.ws.dto.JsonResponse;

/**
 * Created by huyduong on 3/24/2015.
 */
@Path("/drone")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class DroneController {
    @Autowired
    private DroneService droneService;

    @GET
    @Path("/register")
    public JsonResponse register(@QueryParam("id") String id, @QueryParam("name") String name) {
        Drone drone = droneService.registerDrone(id, name);
        return new JsonResponse(drone == null, drone);
    }

    @GET
    @Path("/remove")
    public JsonResponse remove(@QueryParam("id") String id, @QueryParam("name") String name) {
        Drone drone = droneService.removeDrone(id);
        return new JsonResponse(drone == null, drone);
    }

    @GET
    @Path("/{id}")
    public JsonResponse get(@PathParam("id") String id) {
        Drone drone = droneService.getDrone(id);
        return new JsonResponse(drone != null, drone);
    }

    @GET
    @Path("/getall")
    public JsonResponse getAll() {
        Collection<Drone> drones = droneService.getAll();
        return new JsonResponse(drones != null, drones);
    }

    @GET
    @Path("/move")
    public JsonResponse move(@QueryParam("id") String id) {
        Drone drone = droneService.moveDrone(id);
        return new JsonResponse(drone == null, drone);
    }

    @GET
    @Path("/start")
    public JsonResponse start(@QueryParam("id") String id) {
        Drone drone = droneService.startDrone(id);
        return new JsonResponse(drone == null, drone);
    }

    @GET
    @Path("/stop")
    public JsonResponse stop(@QueryParam("id") String id) {
        Drone drone = droneService.stopDrone(id);
        return new JsonResponse(drone == null, drone);
    }
    
    @POST
    @Path("/load-drone-in-view")
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonResponse loadDroneWithinView(BoundingBox boundary) {
        List<Drone> data = droneService.loadDroneWithinView(boundary);
        JsonResponse response = new JsonResponse(data != null, data);
        return response;
    }
}
