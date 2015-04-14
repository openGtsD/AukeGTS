package no.auke.drone.domain.test;

import junit.framework.Assert;
import no.auke.drone.domain.Tracker;
import no.auke.drone.services.TrackerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * Created by huyduong on 4/13/2015.
 */
public class TrackerControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TrackerService trackerService;

    @Test
    public void shouldGetAllTrackers() {
        Collection<Tracker> trackers = trackerService.getAll();
        Assert.assertSame(50,trackers.size());
    }

    @Test
    public void shouldRegisterTracker() {
        Collection<Tracker> trackers = trackerService.getAll();
        Assert.assertSame(50,trackers.size());

        Tracker tracker = trackerService.registerTracker("id","new name");
        Assert.assertNotNull(tracker.getCurrentPosition());
        trackers = trackerService.getAll();
        Assert.assertSame(51,trackers.size());
    }

    @Test
    public void shouldRemoveTracker() {
        Collection<Tracker> trackers = trackerService.getAll();
        Assert.assertSame(50,trackers.size());

        Tracker tracker = trackerService.removeTracker("id");

        trackers = trackerService.getAll();
        Assert.assertSame(50,trackers.size());    }
}
