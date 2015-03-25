package com.auke.drone.ws.controllers;

import com.auke.drone.ws.dto.JsonResponse;
import no.auke.drone.dao.DroneFactory;
import no.auke.drone.dao.impl.SimpleDroneFactory;
import no.auke.drone.domain.Drone;
import no.auke.drone.domain.DroneData;
import no.auke.drone.domain.Observer;
import org.springframework.stereotype.Component;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by huyduong on 3/24/2015.
 */
@Path("/drone")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class DroneController {

    @GET
    @Path("/register")
    public JsonResponse register(@QueryParam("id") String id, @QueryParam("name") String name) {
        Drone drone = new SimpleDroneFactory().createDrone(id,name);
        DroneData.getInstance().register((Observer)drone);
        return new JsonResponse(drone == null, drone);
    }

    @GET
    @Path("/remove")
    public JsonResponse remove(@QueryParam("id") String id, @QueryParam("name") String name) {
        Drone drone = new SimpleDroneFactory().createDrone(id,name);
        DroneData.getInstance().remove((Observer) drone);
        return new JsonResponse(drone == null, drone);
    }

    @GET
    @Path("/getall")
    public JsonResponse getAll() {
        List<Observer> drones = DroneData.getInstance().getDrones();
        return new JsonResponse(drones == null, drones);
    }
}
