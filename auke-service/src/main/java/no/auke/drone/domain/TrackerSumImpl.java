package no.auke.drone.domain;

import no.auke.drone.services.TripService;

public class TrackerSumImpl extends AbstractTrackerBase implements  TrackerSum {
	public TrackerSumImpl() {}

    // number of trackers on this positions
    private int numtrackers;
    
	@Override
	public int getNumtrackers() {
		return numtrackers;
	}

	@Override
	public void setNumtrackers(int numtrackers) {
		this.numtrackers = numtrackers;
	}
	
	@Override
	public void incrementTrackers() {
		this.numtrackers++;
	}


}
