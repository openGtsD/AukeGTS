package no.auke.drone.services.impl;

import no.auke.drone.dao.CRUDDao;
import no.auke.drone.dao.QueryBuilder;
import no.auke.drone.domain.EventData;
import no.auke.drone.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
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
    private CRUDDao<EventData> crudDao;

    @PostConstruct
    public void init() {
        logger.info("initializing EventServiceImpl");
        crudDao.setPersistentClass(EventData.class);
        fetchEvents();
    }

    private List<EventData> eventDatas;

    public static long CALC_FREQUENCY = 5000; // time in milliseconds

    private AtomicBoolean isRunning = new AtomicBoolean();
    private static ExecutorService executor = Executors.newCachedThreadPool();

    public List<EventData> getEventDatas() {
        return eventDatas;
    }

    public void fetchEventData() {
        QueryBuilder timeBuilder = new QueryBuilder().buildSelect("max(timestamp) - 100",EventData.class.getSimpleName());

        QueryBuilder queryBuilder = new QueryBuilder().buildSelect(EventData.class.getSimpleName())
                .buildWhere()
                .buildParam("timestamp")
                .buildMoreThan()
                .buildInnerQuery(timeBuilder.build());

        eventDatas = crudDao.get(queryBuilder.build());
    }

    @Override
    public void fetchEvents() {

        if(isRunning.getAndSet(true)) {
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    long lastStarted = System.currentTimeMillis();
                    while(isRunning.get()) {
                        logger.info("starting fetching data" + isRunning);

                        // querying
                        fetchEventData();

                        if(isRunning.get() && (System.currentTimeMillis() - lastStarted) < CALC_FREQUENCY ) {
                            // sleep for rest time to CALC_FREQUENCY
                            try {
                                Thread.sleep(CALC_FREQUENCY - (System.currentTimeMillis() - lastStarted));
                            } catch (InterruptedException e) {
                            }

                            lastStarted = System.currentTimeMillis();
                        }
                        logger.info("finished fetching data" + isRunning);
                    }

                }


            });
        }
    }
}
