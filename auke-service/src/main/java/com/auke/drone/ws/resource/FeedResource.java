// Copyright (c) 2012 Health Market Science, Inc.
package com.auke.drone.ws.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
    @Path("/get-feed")
    public JsonResponse getFeed() {
        List<PositionUnit> data = liveTrackService.loadAllTrack();
        JsonResponse response = new JsonResponse(data != null, data);
        return response;
    }
    

    
}
