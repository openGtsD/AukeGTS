package no.auke.drone.domain.test;

import junit.framework.Assert;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.Device;
import no.auke.drone.domain.EventData;
import no.auke.drone.domain.Tracker;
import no.auke.drone.services.EventService;
import no.auke.drone.services.TrackerService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by huyduong on 5/14/2015.
 * This should run after TrackerServiceThreadingIntegrationTest
 */
public class TrackerServiceThreading2IntegrationTest extends AbstractIntegrationTest {
    @Autowired
    TrackerService trackerService;

    @Autowired
    EventService eventService;

    @Autowired
    CRUDDao<Device> deviceCRUDDao;

    @Autowired
    CRUDDao<EventData> eventDataCRUDDao;

    @Before
    public void init() {
        deviceCRUDDao.setPersistentClass(Device.class);
        eventDataCRUDDao.setPersistentClass(EventData.class);
    }

    @Ignore // should uncomment when performing testing
    @Test
    public void shouldPollingEventData() throws Exception{
        long time = System.currentTimeMillis();
        Tracker tracker3 = trackerService.registerTracker(TrackerServiceThreadingIntegrationTest.device3Id,"");

        while(true) {
            long currentTime = System.currentTimeMillis();
            if(currentTime - time > 900000) { // more than 15 minutes
                break;
            }


            tracker3 = trackerService.getTracker(TrackerServiceThreadingIntegrationTest.device3Id);

            if(tracker3 == null) {
                System.out.println("tracker3 is passive and removed!!!");
            } else {
                System.out.println("tracker3 is active with position " + tracker3.getCurrentPosition());
                long deltaTime = System.currentTimeMillis()/1000 - tracker3.getCurrentPosition().getTime();
                System.out.println("delta time: " + deltaTime);
            }

            Map eventDataList = eventService.getEventDatas();

            System.out.println("polling data with " + eventDataList.size() + " records " + eventDataList.toString());
            Thread.sleep(3000);

        }

    }
}
