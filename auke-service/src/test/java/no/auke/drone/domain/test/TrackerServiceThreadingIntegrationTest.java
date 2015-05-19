package no.auke.drone.domain.test;

import junit.framework.Assert;
import no.auke.drone.dao.CRUDDao;
import no.auke.drone.domain.Device;
import no.auke.drone.domain.EventData;
import no.auke.drone.services.TrackerService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Properties;

/**
 * Created by huyduong on 5/14/2015.
 */
public class TrackerServiceThreadingIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    TrackerService trackerService;

    @Autowired
    CRUDDao<Device> deviceCRUDDao;

    @Autowired
    CRUDDao<EventData> eventDataCRUDDao;

    public static final String device1Id = "11223344";
    public static final String device2Id = "22334455";
    public static final String device3Id = "33445566";



    @Before
    public void init() {
        deviceCRUDDao.setPersistentClass(Device.class);
        eventDataCRUDDao.setPersistentClass(EventData.class);

        deviceCRUDDao.deleteAll();
        eventDataCRUDDao.deleteAll();
    }

    @Test
    public void shouldCreateNewEventData() throws Exception{
        trackerService.registerTracker(device1Id, "");
        trackerService.registerTracker(device2Id, "");
        trackerService.registerTracker(device3Id, "");

        long time = System.currentTimeMillis();

        for(int i = 0; i < 1000; i++) {
            long currentTime = System.currentTimeMillis();
            if(currentTime - time > 900000) { // more than 15 minutes
                break;
            }

            EventData eventData = new EventData();
            eventData.setDeviceID(device1Id);
            eventData.setAccountID("demo");
            eventData.setLongitude(1121 + i * 3);
            eventData.setLatitude(1131 + i * 3);
            eventData.setAltitude(1141 + i * 3);
            eventData.setStatusCode(1151 + i * 3);
            eventDataCRUDDao.create(eventData);
            System.out.println("created event1 " + eventData.toString());

            EventData eventData2 = new EventData();
            eventData2.setDeviceID(device2Id);
            eventData2.setAccountID("demo");
            eventData2.setLongitude(1121 + i * 3);
            eventData2.setLatitude(1131 + i * 3);
            eventData2.setAltitude(1141 + i * 3);
            eventData2.setStatusCode(1151 + i * 3);
            System.out.println("created event2 " + eventData2.toString());
            eventDataCRUDDao.create(eventData2);

            Thread.sleep(5000);
        }

        Properties properties = new Properties();
        properties.put("accountID","demo");
        properties.put("deviceID",device1Id);

        List<EventData> eventDataList = eventDataCRUDDao.getByProperties(properties);

        for(EventData eventData1: eventDataList) {
            eventDataCRUDDao.delete(eventData1);
        }

        eventDataList = eventDataCRUDDao.getByProperties(properties);
        Assert.assertEquals(0,eventDataList.size());
    }
}
