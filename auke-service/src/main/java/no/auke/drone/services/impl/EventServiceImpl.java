package no.auke.drone.services.impl;

import no.auke.drone.application.impl.SimpleTrackerFactory;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.dao.QueryBuilder;
import no.auke.drone.domain.*;
import no.auke.drone.services.EventService;

import no.auke.drone.services.TrackerService;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by huyduong on 4/24/2015.
 */
@Service
public class EventServiceImpl implements EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    private CRUDDao<EventData> eventCrudDao;

    @Autowired
    private CRUDDao<Device> deviceCrudDao;

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private SimpleTrackerFactory simpleTrackerFactory;

    @PostConstruct
    public void init() {
        logger.info("initializing EventServiceImpl");
        eventCrudDao.setPersistentClass(EventData.class);
        deviceCrudDao.setPersistentClass(Device.class);
        fetchEvents();
    }


    private Map<String,EventData> eventDatas = new ConcurrentHashMap<String,EventData>();

    public static long CALC_FREQUENCY = 5000; // time in milliseconds

    private AtomicBoolean isRunning = new AtomicBoolean();
    private static ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public Map<String,EventData> getEventDatas() {
        if(eventDatas == null) {
            eventDatas = new HashMap<>();
        }
    	return eventDatas;
    }

    @Override    
    public void fetchEventData() {
        QueryBuilder timeBuilder = new QueryBuilder().buildSelect("max(timestamp) - 5000",EventData.class.getSimpleName());

        QueryBuilder queryBuilder = new QueryBuilder().buildSelect(EventData.class.getSimpleName())
                .buildWhere()
                .buildParam("timestamp")
                .buildMoreThan()
                .buildInnerQuery(timeBuilder.build());
        List<EventData> eventFetched = eventCrudDao.get(queryBuilder.build());
        
        eventDatas.clear();
        for(EventData event:eventFetched) {
        	eventDatas.put(event.getDeviceID(), event);
        }
    }
    
	private void updateTrackers() {
        if(logger.isDebugEnabled()) logger.debug("starting updating tracker data from device");

        List<Device> devices = deviceCrudDao.getAll();
        for(Device device : devices) {
            Tracker tracker = trackerService.getTracker(device.getDeviceID());
            if(tracker == null) {
                tracker = simpleTrackerFactory.from(device);
                trackerService.registerTracker(tracker);
            }

            if(device.getIsActive() != null && tracker.isActive() != device.getIsActive()) {
                tracker.setActive(BooleanUtils.isTrue(device.getIsActive()));
                trackerService.updateActiveTracker(tracker);
            }
        }
	}

    @Override
    public void fetchEvents() {

        if(!isRunning.getAndSet(true)) {
            executor.execute(new Runnable() {
            	
                @Override
                public void run() {

                	long lastStarted = System.currentTimeMillis();
                    while(isRunning.get()) {
                        if(logger.isDebugEnabled()) logger.debug("starting fetching data" + isRunning);

                        // querying
                        fetchEventData();

                        updateTrackers();

//                        updateTrackers();

                        if(isRunning.get() && (System.currentTimeMillis() - lastStarted) < CALC_FREQUENCY ) {
                            // sleep for rest time to CALC_FREQUENCY
                            try {
                                Thread.sleep(CALC_FREQUENCY - (System.currentTimeMillis() - lastStarted));
                            } catch (InterruptedException e) {
                            }

                            lastStarted = System.currentTimeMillis();
                        }
                        if(logger.isDebugEnabled()) logger.debug("finished fetching data" + isRunning);
                    }

                }




            });
        }
    }
}
