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
    private CRUDDao<Device> crudDeviceDao;
    @Autowired
    private CRUDDao<EventData> crudEventDao;
    @Autowired
    private EventService eventService;
    @Autowired
    private ZoomLayerService zoomLayerService;

    private static final Logger logger = LoggerFactory.getLogger(TrackerController.class);

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
        logger.info("loading drones within view : " + layerId + " " + zoom);
        Collection<Tracker> data = trackerService.loadWithinView(boundary, zoom, layerId);
        AukeResponse response = new AukeResponse(data != null, data);
        return response;
    }

    
    //
    // LHA: What is this for ?
    // Event service only hold current event for all trackers active
    // To get history, it should be stored on the tracker
    
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
    @Path("/register/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse register(@PathParam("id")String id) {
        Tracker newTracker = trackerService.registerTracker(id, "");
        AukeResponse response = new AukeResponse(newTracker != null, newTracker);
        return response;
    }
    
    @POST
    @Path("/remove/{id}")
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
    
    @GET
    @Path("/get-included-tracker/{id}/{layerId}/{zoom}")
    public AukeResponse getIncludedTrackers(@PathParam("id") String id, @PathParam("layerId")String layerId, @PathParam("zoom")Integer zoom) {
        Collection<String> trackers = trackerService.getTrackerLayer(layerId).getZoomLayer(zoom).getIncludedTrackers(id);
        return new AukeResponse(!CollectionUtils.isEmpty(trackers), trackers);
    }

    @GET
    @Path("/get-trackers/{ids}")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse getTrackersByIds(@PathParam("ids") List<String> ids) {
        Collection<Tracker> trackers = trackerService.getTrackersByIds(ids);
        return new AukeResponse(trackers != null, trackers);
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

    @GET
    @Path("/layers")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse getTrackerLayers() {
        Collection<TrackerLayer> layers = trackerService.getTrackerLayers();
        List<String> result = new ArrayList<>();
        for(TrackerLayer trackerLayer : layers) {
            result.add(trackerLayer.getLayerName());
        }
        return new AukeResponse(result != null, result);
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
        Tracker newTracker = trackerService.remove(id);
        AukeResponse response = new AukeResponse(newTracker != null, newTracker);
        return response;
    }
}
