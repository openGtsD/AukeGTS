package no.auke.drone.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huyduong on 3/24/2015.
 */

public abstract class AbstractTrackerPositionBase extends AbstractTrackerBase {
    private static final Logger logger = LoggerFactory.getLogger(AbstractTrackerPositionBase.class);

    public AbstractTrackerPositionBase() {
        super();
    }

    public AbstractTrackerPositionBase(String id) {
        super(id);
    }

    @Override
    public void setNumtrackers(int numtrackers) {
    }

    @Override
    public int getNumtrackers() {
        return 0;
    }

    @Override
    public void incrementTrackers() {

    }
}
