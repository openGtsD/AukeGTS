// Copyright (c) 2012 Health Market Science, Inc.
package com.auke.drone.ws.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.auke.drone.domain.PositionUnit;
import no.auke.drone.services.LiveTrackServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auke.drone.ws.dto.JsonResponse;

/**
 * @author thaihuynh
 *
 */
@Path("/feed")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class FeedResource {
    
    @Autowired
    private LiveTrackServices liveTrackService;
    
    @GET
    @Path("/get-all")
    public JsonResponse getAllDrones() {
        List<PositionUnit> data = liveTrackService.loadAllTrack();
        JsonResponse response = new JsonResponse(data != null, data);
        return response;
    }
    
    @GET
    @Path("/{droneId}")
    public JsonResponse getDrone(@PathParam("droneId") String droneId) {
        PositionUnit data = liveTrackService.getDrone(droneId);
        JsonResponse response = new JsonResponse(data != null, data);
        return response;
    }

    
}
